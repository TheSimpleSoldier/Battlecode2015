package _teambasherTank.Units.harrassers;

import battlecode.common.*;
import _teambasherTank.Messaging;
import _teambasherTank.Unit;
import _teambasherTank.Units.HarrasserUnit;
import _teambasherTank.Units.Rushers.SoldierRusher;
import _teambasherTank.Utilities;

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
