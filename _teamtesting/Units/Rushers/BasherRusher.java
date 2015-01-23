package _teamtesting.Units.Rushers;

import battlecode.common.*;
import _teamtesting.Messaging;
import _teamtesting.Unit;
import _teamtesting.Units.Basher;
import _teamtesting.Utilities;

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
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
