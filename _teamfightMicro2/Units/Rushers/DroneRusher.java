package _teamfightMicro2.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro2.Unit;
import _teamfightMicro2.Units.Drone;
import _teamfightMicro2.Utilities;

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
