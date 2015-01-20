package _teamSeeding.Units.harrassers;

import battlecode.common.*;
import _teamSeeding.Messaging;
import _teamSeeding.Unit;
import _teamSeeding.Units.HarrasserUnit;
import _teamSeeding.Units.Rushers.SoldierRusher;
import _teamSeeding.Utilities;

public class BasherHarrass extends HarrasserUnit
{
    public BasherHarrass(RobotController rc)
    {
        super(rc);
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfBashersOdd.ordinal(), Messaging.NumbOfBashersEven.ordinal());
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new SoldierRusher(rc);
        }
        return current;
    }
}
