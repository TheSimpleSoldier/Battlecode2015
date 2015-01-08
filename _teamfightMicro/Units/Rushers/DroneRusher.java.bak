package team044.Units.Rushers;

import battlecode.common.*;
import team044.Unit;
import team044.Units.Drone;

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

        target = rc.senseEnemyHQLocation();
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
