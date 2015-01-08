package _teamfightMicro.Units.Rushers;

import battlecode.common.*;

import _teamfightMicro.Unit;
import _teamfightMicro.Units.Tank;

public class TankRusher extends Tank
{
    public TankRusher(RobotController rc)
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
