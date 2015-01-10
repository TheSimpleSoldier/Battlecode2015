package team044.Units.Rushers;

import battlecode.common.*;
import team044.Unit;
import team044.Units.Drone;
import team044.Utilities;

/**
 * Created by fred on 1/8/15.
 */
public class DroneRusher extends Drone {
    public DroneRusher(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        target = Utilities.getRushLocation(rc);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
