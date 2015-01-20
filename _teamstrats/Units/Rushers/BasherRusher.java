package _teamstrats.Units.Rushers;

import battlecode.common.*;
import _teamstrats.Messaging;
import _teamstrats.Unit;
import _teamstrats.Units.Basher;
import _teamstrats.Utilities;

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
