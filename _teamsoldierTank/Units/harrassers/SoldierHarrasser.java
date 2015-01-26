package _teamsoldierTank.Units.harrassers;

import battlecode.common.*;
import _teamsoldierTank.Messaging;
import _teamsoldierTank.Unit;
import _teamsoldierTank.Units.HarrasserUnit;
import _teamsoldierTank.Units.Rushers.SoldierRusher;
import _teamsoldierTank.Utilities;

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
