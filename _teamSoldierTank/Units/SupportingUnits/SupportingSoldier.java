package _teamSoldierTank.Units.SupportingUnits;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import _teamSoldierTank.Messaging;
import _teamSoldierTank.Units.SupportingUnit;

public class SupportingSoldier extends SupportingUnit
{
    public SupportingSoldier(RobotController rc) throws GameActionException
    {
        super(rc);
        group = rc.readBroadcast(Messaging.SoldierGroup.ordinal());
        rc.broadcast(Messaging.SoldierGroup.ordinal(), -1);
        rc.setIndicatorString(0, "Supporting soldier");
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
}
