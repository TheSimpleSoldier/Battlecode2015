package team044.Units.Rushers;

import battlecode.common.*;
import team044.Messaging;
import team044.Unit;
import team044.Units.Basher;

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
