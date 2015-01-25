package _teamsoldierLauncher.Units.Rushers;

import battlecode.common.*;
import _teamsoldierLauncher.Unit;
import _teamsoldierLauncher.Units.Launcher;
import _teamsoldierLauncher.Utilities;

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
