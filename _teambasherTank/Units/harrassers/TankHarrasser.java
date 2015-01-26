package _teambasherTank.Units.harrassers;

import battlecode.common.*;
import _teambasherTank.Messaging;
import _teambasherTank.Unit;
import _teambasherTank.Units.HarrasserUnit;
import _teambasherTank.Units.Rushers.TankRusher;
import _teambasherTank.Utilities;

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
