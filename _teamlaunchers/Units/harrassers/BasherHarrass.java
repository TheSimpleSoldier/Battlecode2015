package _teamlaunchers.Units.harrassers;

import battlecode.common.*;
import _teamlaunchers.Messaging;
import _teamlaunchers.Unit;
import _teamlaunchers.Units.HarrasserUnit;
import _teamlaunchers.Units.Rushers.SoldierRusher;
import _teamlaunchers.Utilities;

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
