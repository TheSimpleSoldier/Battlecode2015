package _teammaster.Units.Rushers;

import battlecode.common.*;
import _teammaster.Messaging;
import _teammaster.Unit;
import _teammaster.Units.Basher;
import _teammaster.Utilities;

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
