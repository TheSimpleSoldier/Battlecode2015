package _teamstreamLaunchers.Structures;

import battlecode.common.*;
import _teamstreamLaunchers.Messaging;
import _teamstreamLaunchers.Structure;
import _teamstreamLaunchers.Utilities;

public class TankFactory extends Structure{
    public TankFactory(RobotController rc)
    {
        super(rc);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (/*rc.readBroadcast(Messaging.ShutOffTankProd.ordinal()) == 0 &&*/ Utilities.spawnUnit(RobotType.TANK, rc))
        {
            return true;
        }
        return false;
    }
}
