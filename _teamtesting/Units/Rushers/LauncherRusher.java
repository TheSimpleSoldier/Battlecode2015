package _teamtesting.Units.Rushers;

import battlecode.common.*;
import _teamtesting.Unit;
import _teamtesting.Units.Launcher;
import _teamtesting.Utilities;

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