public class NODE_sensor implements RobotProgramIntNode{
    ENUM_SENSORS sensor;

    public NODE_sensor(ENUM_SENSORS sensor) {
        this.sensor = sensor;
    }

    @Override
    public int evaluate(Robot robot) {
        switch (sensor) {
            case FUEL_LEFT:
                return robot.getFuel();
            case OPP_LR:
                return robot.getOpponentLR();
            case OPP_FB:
                return robot.getOpponentFB();
            case NUM_BARRELS:
                return robot.numBarrels();
            case BARRELS_LR:
                return robot.getClosestBarrelLR();
            case BARRELS_FB:
                return robot.getClosestBarrelFB();
            case WALL_DIST:
                return robot.getDistanceToWall();
            default:
                return 0;
        }
    }

    public String toString() {
        switch (sensor) {
            case FUEL_LEFT:
                return "[Fuel left]";
            case OPP_LR:
                return "[opponent left-right]";
            case OPP_FB:
                return "[opponent front-back]";
            case NUM_BARRELS:
                return "[number of barrels]";
            case BARRELS_LR:
                return "[barrels left-right]";
            case BARRELS_FB:
                return "[barrels front-back]";
            case WALL_DIST:
                return "[distance to wall]";
            default: return null;
        }
    }
}
