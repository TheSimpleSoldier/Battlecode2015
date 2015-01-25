package _teamlauncherTimingAttack.Units.harrassers;

import battlecode.common.*;
import _teamlauncherTimingAttack.Messaging;
import _teamlauncherTimingAttack.Unit;
import _teamlauncherTimingAttack.Units.HarrasserUnit;
import _teamlauncherTimingAttack.Units.Rushers.TankRusher;
import _teamlauncherTimingAttack.Utilities;

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
