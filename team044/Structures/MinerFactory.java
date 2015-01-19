package team044.Structures;

import battlecode.common.*;
import team044.Messaging;
import team044.Structure;
import team044.Utilities;

public class MinerFactory extends Structure
{
    private int numbOfMiners;
    private int actualMiners = 0;

    public MinerFactory(RobotController rc)
    {
        super(rc);
    }


    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();
        actualMiners = rc.readBroadcast(Messaging.NumbOfMiners.ordinal());
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (Utilities.cutProd(rc))
        {
            return false;
        }
        else if (numbOfMiners > 30 && actualMiners < 20 && Utilities.spawnUnit(RobotType.MINER, rc))
        {
            return true;
        }
        else if (numbOfMiners > 30)
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
