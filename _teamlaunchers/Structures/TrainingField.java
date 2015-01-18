package _teamlaunchers.Structures;

import battlecode.common.*;
import _teamlaunchers.Structure;
import _teamlaunchers.Utilities;

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
