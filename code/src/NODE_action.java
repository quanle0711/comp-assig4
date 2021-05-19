public class NODE_action implements RobotProgramNode {
    ENUM_ACTIONS action;

    public NODE_action(ENUM_ACTIONS action) {
        this.action = action;

    }

    @Override
    public void execute(Robot robot) {
        switch (action) {
            case MOVE:
                robot.move();
                break;
            case WAIT:
                robot.idleWait();
                break;
            case TAKEFUEL:
                robot.takeFuel();
                break;
            case TURNLEFT:
                robot.turnLeft();
                break;
            case TURNRIGHT:
                robot.turnRight();
                break;
            case TURNAROUND:
                robot.turnAround();
                break;
            case SHIELDON:
                robot.setShield(true);
            case SHIELDOFF:
                robot.setShield(false);
            default:
                break;
        }
    }

    public String toString() {
        switch (action) {
            case MOVE:
                return "move";
            case WAIT:
                return "wait";
            case TAKEFUEL:
                return "take fuel";
            case TURNLEFT:
                return "turn left";
            case TURNRIGHT:
                return "turn right";
            case TURNAROUND:
                return "turn around";
            case SHIELDON:
                return "shield on";
            case SHIELDOFF:
                return "shield off";
            default:
                return null;
        }
    }
}





//public class NODE_action {
//    public static NODE_action_tl createTurnLeftNode() {
//        return new NODE_action_tl();
//    }
//
//    public static NODE_action_tr createTurnRightNode() {
//        return new NODE_action_tr();
//    }
//
//    public static NODE_action_wait createWaitNode() {
//        return new NODE_action_wait();
//    }
//
//    public static NODE_action_move createMoveNode() {
//        return new NODE_action_move();
//    }
//
//    public static NODE_action_takeFuel createTakeFuelNode() {
//        return new NODE_action_takeFuel();
//    }
//}
//
//class NODE_action_tl implements RobotProgramNode{
//    @Override
//    public void execute(Robot robot) {
//        robot.turnLeft();
//    }
//
//    public String toString() {
//        return "turn left";
//    }
//}
//
//class NODE_action_tr implements RobotProgramNode{
//    @Override
//    public void execute(Robot robot) {
//        robot.turnRight();
//    }
//
//    public String toString() {
//        return "turn right";
//    }
//}
//class NODE_action_move implements RobotProgramNode{
//    @Override
//    public void execute(Robot robot) {
//        robot.move();
//    }
//
//    public String toString() {
//        return "move";
//    }
//}
//
//class NODE_action_wait implements RobotProgramNode{
//    @Override
//    public void execute(Robot robot) {
//        robot.idleWait();
//    }
//
//    public String toString() {
//        return "wait";
//    }
//}
//
//class NODE_action_takeFuel implements RobotProgramNode{
//    @Override
//    public void execute(Robot robot) {
//        robot.takeFuel();
//    }
//
//    public String toString() {
//        return "takeFuel";
//    }
//}
//
