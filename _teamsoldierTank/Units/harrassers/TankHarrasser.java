package _teamsoldierTank.Units.harrassers;

import battlecode.common.*;
import _teamsoldierTank.Messaging;
import _teamsoldierTank.Unit;
import _teamsoldierTank.Units.HarrasserUnit;
import _teamsoldierTank.Units.Rushers.TankRusher;
import _teamsoldierTank.Utilities;

public class TankHarrasser extends HarrasserUnit
{
    public TankHarrasser(RobotController rc)
    {
        super(rc);
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfTanksOdd.ordinal(), Messaging.NumbOfTanksEven.ordinal());
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new TankRusher(rc);
        }
        return current;
    }
}
