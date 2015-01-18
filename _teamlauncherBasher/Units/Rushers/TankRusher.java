package _teamlauncherBasher.Units.Rushers;

import battlecode.common.*;

import _teamlauncherBasher.Unit;
import _teamlauncherBasher.Units.Tank;
import _teamlauncherBasher.Utilities;

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
