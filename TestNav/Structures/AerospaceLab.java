package TestNav.Structures;

import battlecode.common.*;
import TestNav.Structure;
import TestNav.Utilities;

public class AerospaceLab extends Structure
{
    public AerospaceLab(RobotController rc)
    {
        super(rc);
    }

    // any code to override base structure methods goes here
    public boolean carryOutAbility() throws GameActionException
    {
        if (Utilities.spawnUnit(RobotType.LAUNCHER, rc))
        {
            return true;
        }
        return false;
    }
}
