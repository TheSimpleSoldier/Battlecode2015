package _teamoffense.Units.Rushers;

import battlecode.common.*;
import _teamoffense.Messaging;
import _teamoffense.Unit;
import _teamoffense.Units.Basher;
import _teamoffense.Utilities;

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
