package theSimpleSoldier.Structures;

import battlecode.common.*;
import theSimpleSoldier.Structure;
import theSimpleSoldier.Utilities;

public class Barracks extends Structure
{
    public Barracks(RobotController rc)
    {
        this.rc = rc;
    }

    // overridden methods go here

    public boolean carryOutAbility() throws GameActionException
    {
        if (rc.getTeamOre() > 600 && Utilities.spawnUnit(RobotType.BASHER, rc))
        {
            return true;
        }
        return false;
    }
}
