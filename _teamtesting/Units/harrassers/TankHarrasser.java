package _teamtesting.Units.harrassers;

import battlecode.common.*;
import _teamtesting.Messaging;
import _teamtesting.Unit;
import _teamtesting.Units.HarrasserUnit;
import _teamtesting.Units.Rushers.TankRusher;
import _teamtesting.Utilities;

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
