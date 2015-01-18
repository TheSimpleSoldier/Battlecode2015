package _teamlaunchers.Structures;

import battlecode.common.*;
import _teamlaunchers.Structure;
import _teamlaunchers.Utilities;

public class TankFactory extends Structure{
    public TankFactory(RobotController rc)
    {
        super(rc);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (Utilities.spawnUnit(RobotType.TANK, rc))
        {
            return true;
        }
        return false;
    }
}
