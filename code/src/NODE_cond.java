public class NODE_cond implements RobotProgramBooleanNode{
    ENUM_RELOP relop;
    NODE_sensor sensor;
    int num;

    public NODE_cond(ENUM_RELOP relop, NODE_sensor sensor, int num) {
        this.relop = relop;
        this.sensor = sensor;
        this.num = num;
    }

    @Override
    public boolean evaluate(Robot robot) {
        switch (relop) {
            case GT:
                return sensor.evaluate(robot) > num;
            case LT:
                return sensor.evaluate(robot) < num;
            case EQ:
                return sensor.evaluate(robot) == num;
            default:
                return false;
        }
    }

    public String toString() {
        switch (relop) {
            case GT:
                return sensor + " is greater than " + num;
            case LT:
                return sensor + " is less than " + num;
            case EQ:
                return sensor + " is equals to " + num;
            default:
                return null;
        }
    }
}
