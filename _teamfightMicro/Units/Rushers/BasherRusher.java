package _teamfightMicro.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro.Messaging;
import _teamfightMicro.Unit;
import _teamfightMicro.Units.Basher;

public class BasherRusher extends Basher
{
    public BasherRusher(RobotController rc)
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
