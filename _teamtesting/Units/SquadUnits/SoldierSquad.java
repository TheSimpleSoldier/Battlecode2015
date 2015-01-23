package _teamtesting.Units.SquadUnits;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import _teamtesting.Messaging;
import _teamtesting.Unit;
import _teamtesting.Units.Rushers.LauncherRusher;
import _teamtesting.Units.Rushers.SoldierRusher;
import _teamtesting.Units.SquadUnit;

public class SoldierSquad extends SquadUnit
{
    public SoldierSquad(RobotController rc) throws GameActionException
    {
        super(rc);
        group = rc.readBroadcast(Messaging.SoldierGroup.ordinal());
        rc.broadcast(Messaging.SoldierGroup.ordinal(), -1);
        rc.setIndicatorString(0, "Squad Tank group: " + group);
    }
    public void collectData() throws GameActionException
    {
        super.collectData();

        if (group < 1)
        {
            group = rc.readBroadcast(Messaging.SoldierGroup.ordinal());
            rc.broadcast(Messaging.SoldierGroup.ordinal(), -1);
        }
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new SoldierRusher(rc);
        }
        return current;
    }
}
