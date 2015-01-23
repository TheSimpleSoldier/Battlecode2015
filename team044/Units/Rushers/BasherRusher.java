package team044.Units.Rushers;

import battlecode.common.*;
import team044.Messaging;
import team044.Unit;
import team044.Units.Basher;
import team044.Utilities;

public class BasherRusher extends Basher
{
    public BasherRusher(RobotController rc)
    {
        super(rc);

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        target = Utilities.getRushLocation(rc);

        rc.setIndicatorString(1, "Target: " + target);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
