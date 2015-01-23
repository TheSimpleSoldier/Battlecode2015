package _teamtesting.Structures;

import battlecode.common.*;
import _teamtesting.Messaging;
import _teamtesting.Structure;
import _teamtesting.Utilities;

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