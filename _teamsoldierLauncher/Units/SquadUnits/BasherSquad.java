package _teamsoldierLauncher.Units.SquadUnits;

import battlecode.common.*;
import _teamsoldierLauncher.Messaging;
import _teamsoldierLauncher.Unit;
import _teamsoldierLauncher.Units.Rushers.BasherRusher;
import _teamsoldierLauncher.Units.Rushers.LauncherRusher;
import _teamsoldierLauncher.Units.SquadUnit;

public class BasherSquad extends SquadUnit
{
    public BasherSquad(RobotController rc) throws GameActionException
    {
        super(rc);
        group = rc.readBroadcast(Messaging.BasherGroup.ordinal());
        rc.broadcast(Messaging.BasherGroup.ordinal(), -1);
        rc.setIndicatorString(0, "Squad Basher group: " + group);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        if (group < 1)
        {
            group = rc.readBroadcast(Messaging.BasherGroup.ordinal());
            rc.broadcast(Messaging.BasherGroup.ordinal(), -1);
        }
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basherFightMicro();
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new BasherRusher(rc);
        }
        return current;
    }
}
