package team044.Structures;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team044.Structure;
import team044.Utilities;

public class Helipad extends Structure
{
    public Helipad(RobotController rc)
    {
        this.rc = rc;
    }

    @Override
    public boolean carryOutAbility() throws GameActionException
    {
        return Utilities.spawnUnit(RobotType.DRONE, rc);
    }
}
