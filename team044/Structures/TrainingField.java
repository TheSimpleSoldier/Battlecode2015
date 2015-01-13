package team044.Structures;

import battlecode.common.*;
import team044.Structure;
import team044.Utilities;

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
        if (Utilities.spawnUnit(RobotType.TANK, rc))
        {
            return true;
        }
        return false;
    }
}
