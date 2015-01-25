package _teamlauncherTimingAttack.Units.SquadUnits;

import battlecode.common.*;
import _teamlauncherTimingAttack.Messaging;
import _teamlauncherTimingAttack.Unit;
import _teamlauncherTimingAttack.Units.Rushers.LauncherRusher;
import _teamlauncherTimingAttack.Units.Rushers.TankRusher;
import _teamlauncherTimingAttack.Units.SquadUnit;

public class TankSquad extends SquadUnit
{
    public TankSquad(RobotController rc) throws GameActionException
    {
        super(rc);
        group = rc.readBroadcast(Messaging.TankGroup.ordinal());
        rc.broadcast(Messaging.TankGroup.ordinal(), -1);
        rc.setIndicatorString(0, "Squad Tank group:" + group);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        if (group < 1)
        {
            group = rc.readBroadcast(Messaging.TankGroup.ordinal());
            rc.broadcast(Messaging.TankGroup.ordinal(), -1);
            rc.setIndicatorString(0, "Squad Tank group:" + group);
        }
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
