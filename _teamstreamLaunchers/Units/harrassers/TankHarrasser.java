package _teamstreamLaunchers.Units.harrassers;

import battlecode.common.*;
import _teamstreamLaunchers.Messaging;
import _teamstreamLaunchers.Unit;
import _teamstreamLaunchers.Units.HarrasserUnit;
import _teamstreamLaunchers.Units.Rushers.TankRusher;
import _teamstreamLaunchers.Utilities;

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
