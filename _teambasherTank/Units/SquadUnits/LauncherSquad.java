package _teambasherTank.Units.SquadUnits;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import _teambasherTank.BuildOrderMessaging;
import _teambasherTank.Messaging;
import _teambasherTank.Unit;
import _teambasherTank.Units.Rushers.LauncherRusher;
import _teambasherTank.Units.SquadUnit;

public class LauncherSquad extends SquadUnit
{
    public LauncherSquad(RobotController rc) throws GameActionException
    {
        super(rc);
        group = rc.readBroadcast(Messaging.LauncherGroup.ordinal());
        rc.broadcast(Messaging.LauncherGroup.ordinal(), -1);
        rc.setIndicatorString(0, "Squad Launcher group: " + group);
        range = 35;
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        if (group < 1)
        {
            group = rc.readBroadcast(Messaging.LauncherGroup.ordinal());
            rc.broadcast(Messaging.LauncherGroup.ordinal(), -1);
        }
    }

    public boolean fight() throws GameActionException
    {
        return fighter.launcherAttack(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new LauncherRusher(rc);
        }
        return current;
    }
}
