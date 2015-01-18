package _teamlauncherBasher.Units.Rushers;

import battlecode.common.*;
import _teamlauncherBasher.Unit;
import _teamlauncherBasher.Units.Launcher;
import _teamlauncherBasher.Utilities;

public class LauncherRusher extends Launcher
{
    public LauncherRusher(RobotController rc)
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
