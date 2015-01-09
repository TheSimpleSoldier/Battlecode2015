package _teamfightMicro.Structures;

import battlecode.common.*;
import _teamfightMicro.Structure;
import _teamfightMicro.Utilities;

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
