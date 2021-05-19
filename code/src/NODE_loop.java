public class NODE_loop implements RobotProgramNode{
    public NODE_block block;
    @Override
    public void execute(Robot robot) {
        block.execute(robot);
    }

    public String toString() {
        return "loop: \n" + this.block;
    }
}
