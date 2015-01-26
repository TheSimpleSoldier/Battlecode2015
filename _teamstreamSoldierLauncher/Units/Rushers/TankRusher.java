package _teamstreamSoldierLauncher.Units.Rushers;

import battlecode.common.*;

import _teamstreamSoldierLauncher.Unit;
import _teamstreamSoldierLauncher.Units.Tank;
import _teamstreamSoldierLauncher.Utilities;

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
