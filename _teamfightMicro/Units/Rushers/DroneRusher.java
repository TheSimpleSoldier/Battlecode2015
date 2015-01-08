package _teamfightMicro.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro.Unit;
import _teamfightMicro.Units.Drone;

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
