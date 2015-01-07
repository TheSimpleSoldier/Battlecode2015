package team044.Structures;

import battlecode.common.*;
import team044.Structure;
import team044.Utilities;

public class TankFactory extends Structure{
    public TankFactory(RobotController rc)
    {
        this.rc = rc;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (Utilities.spawnUnit(RobotType.TANK, rc))
        {
            return true;
        }
        return false;
    }
}
