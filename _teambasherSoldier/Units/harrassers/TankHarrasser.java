package _teambasherSoldier.Units.harrassers;

import battlecode.common.*;
import _teambasherSoldier.Messaging;
import _teambasherSoldier.Unit;
import _teambasherSoldier.Units.HarrasserUnit;
import _teambasherSoldier.Units.Rushers.TankRusher;
import _teambasherSoldier.Utilities;

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
