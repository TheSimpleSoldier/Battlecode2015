package _teamtesting.Units.harrassers;

import battlecode.common.*;
import _teamtesting.Messaging;
import _teamtesting.Unit;
import _teamtesting.Units.HarrasserUnit;
import _teamtesting.Units.Rushers.SoldierRusher;
import _teamtesting.Utilities;

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
