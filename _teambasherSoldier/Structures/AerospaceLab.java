package _teambasherSoldier.Structures;

import battlecode.common.*;
import _teambasherSoldier.Structure;
import _teambasherSoldier.Utilities;

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
