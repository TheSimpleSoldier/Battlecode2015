package team044.Units.SupportingUnits;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team044.Messaging;
import team044.Units.SupportingUnit;

public class SupportingSoldier extends SupportingUnit
{
    public SupportingSoldier(RobotController rc) throws GameActionException
    {
        super(rc);
        group = rc.readBroadcast(Messaging.SoldierGroup.ordinal());
        rc.broadcast(Messaging.SoldierGroup.ordinal(), -1);
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
