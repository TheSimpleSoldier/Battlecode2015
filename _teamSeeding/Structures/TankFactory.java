package _teamSeeding.Structures;

import battlecode.common.*;
import _teamSeeding.Messaging;
import _teamSeeding.Structure;
import _teamSeeding.Utilities;

public class TankFactory extends Structure{
    public TankFactory(RobotController rc)
    {
        super(rc);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (rc.readBroadcast(Messaging.ShutOffTankProd.ordinal()) == 0 && Utilities.spawnUnit(RobotType.TANK, rc))
        {
            return true;
        }
        return false;
    }
}
