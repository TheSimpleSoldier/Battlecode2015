package team044.Units.SquadUnits;

import battlecode.common.*;
import team044.Messaging;
import team044.Units.SquadUnit;

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
        }
    }
}
