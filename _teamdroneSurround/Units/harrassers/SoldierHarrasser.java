package _teamdroneSurround.Units.harrassers;

import battlecode.common.*;
import _teamdroneSurround.Messaging;
import _teamdroneSurround.Unit;
import _teamdroneSurround.Units.HarrasserUnit;
import _teamdroneSurround.Units.Rushers.SoldierRusher;
import _teamdroneSurround.Utilities;

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
