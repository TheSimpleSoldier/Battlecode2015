package _teamstreamLaunchers.Units.Rushers;

import battlecode.common.*;
import _teamstreamLaunchers.Unit;
import _teamstreamLaunchers.Units.Drone;
import _teamstreamLaunchers.Utilities;

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

    public boolean fight() throws GameActionException
    {
        return fighter.advancedFightMicro(nearByEnemies);
    }
}
