package _teambasherSoldier.Units.SquadUnits;

import battlecode.common.*;
import _teambasherSoldier.Messaging;
import _teambasherSoldier.Unit;
import _teambasherSoldier.Units.Rushers.LauncherRusher;
import _teambasherSoldier.Units.Rushers.TankRusher;
import _teambasherSoldier.Units.SquadUnit;

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
