package _teamstreamSoldierLauncher.Structures;

import battlecode.common.*;
import _teamstreamSoldierLauncher.Structure;
import _teamstreamSoldierLauncher.Utilities;

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
