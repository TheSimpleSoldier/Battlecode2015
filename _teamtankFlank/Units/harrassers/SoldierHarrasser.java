package _teamtankFlank.Units.harrassers;

import battlecode.common.*;
import _teamtankFlank.Messaging;
import _teamtankFlank.Unit;
import _teamtankFlank.Units.HarrasserUnit;
import _teamtankFlank.Units.Rushers.SoldierRusher;
import _teamtankFlank.Utilities;

public class SoldierHarrasser extends HarrasserUnit
{
    public SoldierHarrasser(RobotController rc)
    {
        super(rc);
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfSoldiersOdd.ordinal(), Messaging.NumbOfSoldiersEven.ordinal());
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new SoldierRusher(rc);
        }
        return current;
    }
}
