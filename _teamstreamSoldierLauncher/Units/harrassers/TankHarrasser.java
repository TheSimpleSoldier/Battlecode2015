package _teamstreamSoldierLauncher.Units.harrassers;

import battlecode.common.*;
import _teamstreamSoldierLauncher.Messaging;
import _teamstreamSoldierLauncher.Unit;
import _teamstreamSoldierLauncher.Units.HarrasserUnit;
import _teamstreamSoldierLauncher.Units.Rushers.TankRusher;
import _teamstreamSoldierLauncher.Utilities;

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
