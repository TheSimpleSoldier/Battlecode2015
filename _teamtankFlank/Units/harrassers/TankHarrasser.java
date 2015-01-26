package _teamtankFlank.Units.harrassers;

import battlecode.common.*;
import _teamtankFlank.Messaging;
import _teamtankFlank.Unit;
import _teamtankFlank.Units.HarrasserUnit;
import _teamtankFlank.Units.Rushers.TankRusher;
import _teamtankFlank.Utilities;

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
