package _teamSeeding.Units.Rushers;

import battlecode.common.*;
import _teamSeeding.Messaging;
import _teamSeeding.Unit;
import _teamSeeding.Units.Basher;
import _teamSeeding.Utilities;

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
