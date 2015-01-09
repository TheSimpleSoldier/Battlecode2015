package _teamfightMicro.Structures;

import battlecode.common.*;
import _teamfightMicro.Structure;
import _teamfightMicro.Utilities;

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
