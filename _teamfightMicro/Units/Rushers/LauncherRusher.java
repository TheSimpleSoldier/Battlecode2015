package _teamfightMicro.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro.Unit;
import _teamfightMicro.Units.Launcher;

public class LauncherRusher extends Launcher
{
    public LauncherRusher(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        target = rc.senseEnemyHQLocation();
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
