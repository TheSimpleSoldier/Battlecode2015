package _teamsoldierLauncher.Units.harrassers;

import battlecode.common.*;
import _teamsoldierLauncher.Messaging;
import _teamsoldierLauncher.Unit;
import _teamsoldierLauncher.Units.HarrasserUnit;
import _teamsoldierLauncher.Units.Rushers.TankRusher;
import _teamsoldierLauncher.Utilities;

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
