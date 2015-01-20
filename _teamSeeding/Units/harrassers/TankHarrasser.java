package _teamSeeding.Units.harrassers;

import battlecode.common.*;
import _teamSeeding.Messaging;
import _teamSeeding.Unit;
import _teamSeeding.Units.HarrasserUnit;
import _teamSeeding.Units.Rushers.TankRusher;
import _teamSeeding.Utilities;

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
