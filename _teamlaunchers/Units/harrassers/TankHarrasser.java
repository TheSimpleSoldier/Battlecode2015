package _teamlaunchers.Units.harrassers;

import battlecode.common.*;
import _teamlaunchers.Messaging;
import _teamlaunchers.Unit;
import _teamlaunchers.Units.HarrasserUnit;
import _teamlaunchers.Units.Rushers.TankRusher;
import _teamlaunchers.Utilities;

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
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new TankRusher(rc);
        }
        return current;
    }
}
