

import java.util.ArrayList;

public class NODE_program implements RobotProgramNode{
    ArrayList<RobotProgramNode> children = new ArrayList<>(); //arraylist for the * (multiple) requirement

    public NODE_program () {
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (RobotProgramNode child : children) {
            str.append(child.toString() + "\n");
        }

        return str.toString();
    }
}
