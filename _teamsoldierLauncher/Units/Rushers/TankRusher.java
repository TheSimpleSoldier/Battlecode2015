package _teamsoldierLauncher.Units.Rushers;

import battlecode.common.*;

import _teamsoldierLauncher.Unit;
import _teamsoldierLauncher.Units.Tank;
import _teamsoldierLauncher.Utilities;

public class TankRusher extends Tank
{
    public TankRusher(RobotController rc)
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
