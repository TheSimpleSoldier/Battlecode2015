package _teamfightMicro2.Structures;

import battlecode.common.*;
import _teamfightMicro2.Structure;
import _teamfightMicro2.Utilities;

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
