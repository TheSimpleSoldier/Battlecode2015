package _teamlauncherTimingAttack.Structures;

import battlecode.common.*;
import _teamlauncherTimingAttack.Structure;
import _teamlauncherTimingAttack.Utilities;

public class TrainingField extends Structure {
    public TrainingField(RobotController rc)
    {
        super(rc);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (rc.hasCommander())
        {
            return false;
        }
        if (Utilities.spawnUnit(RobotType.COMMANDER, rc))
        {
            return true;
        }
        return false;
    }
}
