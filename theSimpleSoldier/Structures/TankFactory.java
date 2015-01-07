package theSimpleSoldier.Structures;

import battlecode.common.*;
import theSimpleSoldier.Structure;
import theSimpleSoldier.Utilities;

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
