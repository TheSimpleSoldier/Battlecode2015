package _teamfightMicro2.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro2.Unit;
import _teamfightMicro2.Units.Launcher;
import _teamfightMicro2.Utilities;

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
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
