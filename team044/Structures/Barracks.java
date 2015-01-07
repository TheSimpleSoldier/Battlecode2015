package team044.Structures;

import battlecode.common.*;
import team044.Structure;
import team044.Utilities;

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
