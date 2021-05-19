import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;
/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

			scan.close();
			System.out.println("parsed correctly");
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		System.out.println("helloworld");
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				System.out.println("Parsing......");
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns

	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");

	//add more
	static Pattern ACTION = Pattern.compile("move|turnL|turnR|takeFuel|wait|turnAround|shieldOn|shieldOff");
	static Pattern SENSOR = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static Pattern RELOP = Pattern.compile("lt|gt|eq");

	static Pattern LOOP = Pattern.compile("loop");
	static Pattern IF = Pattern.compile("if");
	static Pattern WHILE = Pattern.compile("while");
	/**
	 * See assignment handout for the grammar.
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE
		NODE_program root = new NODE_program();
		while (s.hasNext()) {
			if (s.hasNext(ACTION)) {
				RobotProgramNode action = parseAction(s);
				if (action != null) root.addChild(action);
			}
			else if (s.hasNext(LOOP)) {
				RobotProgramNode loop = parseLoop(s);
				if (loop != null) root.addChild(loop);
			}

			else if (s.hasNext(WHILE)) {
				RobotProgramNode _while = parseWhile(s);
				if (_while != null) root.addChild(_while);
			}

			else if (s.hasNext(IF)) {
				RobotProgramNode _if = parseIf(s);
				if (_if != null) root.addChild(_if);
			}
			else {fail("parseProgram failed: invalid token",s);}
		}

		return root;
	}

	static RobotProgramNode parseLoop(Scanner s) {
		s.next(); //ignore 'loop' token as already checked
		NODE_loop loop = new NODE_loop();
		loop.block = parseBlock(s);
		return loop;
	}

	static RobotProgramNode parseWhile(Scanner s) {
		s.next(); //ignore 'while' token as already checked
		NODE_while _while = new NODE_while();

		require(OPENPAREN, "parseWhile failed: missing '('",s);
		_while.cond = parseCond(s);
		require(CLOSEPAREN, "parseWhile failed: missing ')'",s);

		_while.block = parseBlock(s);
		return _while;
	}

	static RobotProgramNode parseIf(Scanner s) {
		s.next(); //ignore 'if' token as already checked
		NODE_if _while = new NODE_if();

		require(OPENPAREN, "parseIf failed: missing '('",s);
		_while.cond = parseCond(s);
		require(CLOSEPAREN, "parseIf failed: missing ')'",s);

		_while.block = parseBlock(s);
		return _while;
	}



	static NODE_cond parseCond(Scanner s) {
		//RELOP operator
		if (s.hasNext(RELOP)) {

			ENUM_RELOP relop = parseRelop(s);
			require(OPENPAREN, "parseCond failed: missing '('",s);
			NODE_sensor sensor = parseSensor(s);
			require(",", "parseCond failed: missing ','",s);
			int num = requireInt(NUMPAT,"parseCond failed: invalid number",s);
			require(CLOSEPAREN, "parseCond failed: missing ')'",s);

			if (relop != null && sensor != null) {
				return new NODE_cond(relop, sensor, num);
			}
		}
		else {
			fail("parseCond failed: invalid or missing cond",s);
			return null;
		}

		return null;
	}

	static NODE_sensor parseSensor(Scanner s) {
		String next = require(SENSOR,"parseSensor failed: invalid sensor", s);
		NODE_sensor result;
		ENUM_SENSORS sensor;

		if (next.equals("fuelLeft")) sensor = ENUM_SENSORS.FUEL_LEFT;
		else if (next.equals("oppLR")) sensor = ENUM_SENSORS.OPP_LR;
		else if (next.equals("oppFB")) sensor = ENUM_SENSORS.OPP_FB;
		else if (next.equals("numBarrels")) sensor = ENUM_SENSORS.NUM_BARRELS;
		else if (next.equals("barrelLR")) sensor = ENUM_SENSORS.BARRELS_LR;
		else if (next.equals("barrelFB")) sensor = ENUM_SENSORS.BARRELS_FB;
		else if (next.equals("wallDist")) sensor = ENUM_SENSORS.WALL_DIST;
		else {
			fail("parseSensor failed: invalid sensor", s);
			return null;
		}

		result = new NODE_sensor(sensor);
		return result;
	}

	static ENUM_RELOP parseRelop(Scanner s) {
		String next = s.next();
		ENUM_RELOP relop;
		if (next.equals("gt")) relop = ENUM_RELOP.GT;
		else if (next.equals("lt")) relop = ENUM_RELOP.LT;
		else if (next.equals("eq")) relop = ENUM_RELOP.EQ;
		else {
			fail("parseRelop failed: invalid relop", s);
			return null;
		}
		return relop;
	}


	static NODE_block parseBlock(Scanner s) {
		NODE_block block = new NODE_block();
		require(OPENBRACE,"parseBlock failed: missing '{' ",s);
		while (s.hasNext() && !s.hasNext(CLOSEBRACE)) {
			if (s.hasNext(ACTION)) {
				RobotProgramNode action = parseAction(s);
				if (action != null) block.addChild(action);
			}
			else if (s.hasNext(LOOP)) {
				RobotProgramNode loop = parseLoop(s);
				if (loop != null) block.addChild(loop);
			}
			else if (s.hasNext(WHILE)) {
				RobotProgramNode _while = parseWhile(s);
				if (_while != null) block.addChild(_while);
			}

			else if (s.hasNext(IF)) {
				RobotProgramNode _if = parseIf(s);
				if (_if != null) block.addChild(_if);
			}
			else {fail("parseBlock failed: invalid token",s);}
		}

		require(CLOSEBRACE,"parseBlock failed: missing '}' ",s);

		if (block.isEmpty()) {
			fail("parseBlock failed: Empty block", s);
			return null;
		}

		return block;
	}

	static RobotProgramNode parseAction(Scanner s) {
		String next= s.next();
		RobotProgramNode result;
		ENUM_ACTIONS action;

		if (next.equals("turnL")) action = ENUM_ACTIONS.TURNLEFT;
		else if (next.equals("turnR")) action = ENUM_ACTIONS.TURNRIGHT;
		else if (next.equals("wait")) action = ENUM_ACTIONS.WAIT;
		else if (next.equals("move")) action = ENUM_ACTIONS.MOVE;
		else if (next.equals("takeFuel")) action = ENUM_ACTIONS.TAKEFUEL;
		else if (next.equals("turnAround")) action = ENUM_ACTIONS.TURNAROUND;
		else if (next.equals("shieldOn")) action = ENUM_ACTIONS.SHIELDON;
		else if (next.equals("shieldOff")) action = ENUM_ACTIONS.SHIELDOFF;
		else {
			fail("parseAction failed: invalid action", s);
			return null;
		}

		require(";","parseAction failed: missing ';' ", s);
		result = new NODE_action(action);
		return result;
	}

	// utility methods for the parser

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public (or private)

