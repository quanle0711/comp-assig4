public class NODE_while implements RobotProgramNode{
    public NODE_cond cond;
    public NODE_block block;

    @Override
    public void execute(Robot robot) {
        if (cond.evaluate(robot)) {
            block.execute(robot);
        }
    }

    public String toString() {
        return "while ( " + this.cond + " )" + this.block;
    }
}
