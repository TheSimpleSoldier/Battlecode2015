package _teamstrats.Structures;

import battlecode.common.*;
import _teamstrats.Structure;
import _teamstrats.Utilities;

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
