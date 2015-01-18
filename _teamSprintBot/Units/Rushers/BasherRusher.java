package _teamSprintBot.Units.Rushers;

import battlecode.common.*;
import _teamSprintBot.Messaging;
import _teamSprintBot.Unit;
import _teamSprintBot.Units.Basher;
import _teamSprintBot.Utilities;

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
