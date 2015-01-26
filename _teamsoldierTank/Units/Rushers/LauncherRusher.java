package _teamsoldierTank.Units.Rushers;

import battlecode.common.*;
import _teamsoldierTank.Unit;
import _teamsoldierTank.Units.Launcher;
import _teamsoldierTank.Utilities;

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
