package _teamstreamLaunchers.Units.harrassers;

import battlecode.common.*;
import _teamstreamLaunchers.Messaging;
import _teamstreamLaunchers.Unit;
import _teamstreamLaunchers.Units.HarrasserUnit;
import _teamstreamLaunchers.Units.Rushers.SoldierRusher;
import _teamstreamLaunchers.Utilities;

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
