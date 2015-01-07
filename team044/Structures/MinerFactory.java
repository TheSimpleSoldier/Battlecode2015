package team044.Structures;

import battlecode.common.*;
import team044.Structure;
import team044.Utilities;

public class MinerFactory extends Structure
{
    private int numbOfMiners;
    public MinerFactory(RobotController rc)
    {
        this.rc = rc;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (numbOfMiners > 9)
        {
            return false;
        }
        else if (Utilities.spawnUnit(RobotType.MINER, rc))
        {
            numbOfMiners++;
            return true;
        }
        return false;
    }
}
