public class NODE_if implements RobotProgramNode{
    public NODE_cond cond;
    public NODE_block block;

    @Override
    public void execute(Robot robot) {
        if (cond.evaluate(robot)) {
            block.execute(robot);
        }
    }

    public String toString() {
        return "if ( " + this.cond + " )" + this.block;
    }
}
