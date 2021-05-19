

import java.util.ArrayList;

public class NODE_block implements RobotProgramNode{

    ArrayList<RobotProgramNode> children = new ArrayList<>(); //arraylist for the * (multiple) requirement

    NODE_block () {
    }

    public void addChild(RobotProgramNode node) {
        children.add(node);
    }

    @Override
    public void execute(Robot robot) {
        for (RobotProgramNode child : children) {
            child.execute(robot);
        }
    }


    public String toString() {
        StringBuilder str = new StringBuilder("Entering Block: \n");
        for (RobotProgramNode child : children) {
            str.append("--" + child.toString() + "\n");
        }
        str.append("Exiting Block:");
        return str.toString();
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }
}
