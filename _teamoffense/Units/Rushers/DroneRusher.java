package _teamoffense.Units.Rushers;

import battlecode.common.*;
import _teamoffense.Unit;
import _teamoffense.Units.Drone;
import _teamoffense.Utilities;

public class DroneRusher extends Drone {
    public DroneRusher(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        target = Utilities.getRushLocation(rc);

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
