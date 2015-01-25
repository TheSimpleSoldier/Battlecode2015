package _teamlauncherTimingAttack.Units.harrassers;

import battlecode.common.*;
import _teamlauncherTimingAttack.Messaging;
import _teamlauncherTimingAttack.Unit;
import _teamlauncherTimingAttack.Units.HarrasserUnit;
import _teamlauncherTimingAttack.Units.Rushers.SoldierRusher;
import _teamlauncherTimingAttack.Utilities;

public class SoldierHarrasser extends HarrasserUnit
{
    public SoldierHarrasser(RobotController rc)
    {
        super(rc);
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfSoldiersOdd.ordinal(), Messaging.NumbOfSoldiersEven.ordinal());
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new SoldierRusher(rc);
        }
        return current;
    }
}
