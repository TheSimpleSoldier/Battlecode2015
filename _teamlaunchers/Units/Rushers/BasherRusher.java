package _teamlaunchers.Units.Rushers;

import battlecode.common.*;
import _teamlaunchers.Messaging;
import _teamlaunchers.Unit;
import _teamlaunchers.Units.Basher;
import _teamlaunchers.Utilities;

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
