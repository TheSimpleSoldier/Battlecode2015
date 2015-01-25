package _teamSoldierTank.Units.harrassers;

import battlecode.common.*;
import _teamSoldierTank.Messaging;
import _teamSoldierTank.Unit;
import _teamSoldierTank.Units.HarrasserUnit;
import _teamSoldierTank.Units.Rushers.TankRusher;
import _teamSoldierTank.Utilities;

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
