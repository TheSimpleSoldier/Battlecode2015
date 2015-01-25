package _teamstreamSoldierLauncher.Units.Rushers;

import battlecode.common.*;
import _teamstreamSoldierLauncher.Unit;
import _teamstreamSoldierLauncher.Units.Drone;
import _teamstreamSoldierLauncher.Utilities;

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
