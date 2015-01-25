package _teambasherSoldier.Units.harrassers;

import battlecode.common.*;
import _teambasherSoldier.Messaging;
import _teambasherSoldier.Unit;
import _teambasherSoldier.Units.HarrasserUnit;
import _teambasherSoldier.Units.Rushers.SoldierRusher;
import _teambasherSoldier.Utilities;

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
