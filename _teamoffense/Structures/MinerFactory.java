package _teamoffense.Structures;

import battlecode.common.*;
import _teamoffense.Messaging;
import _teamoffense.Structure;
import _teamoffense.Utilities;

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
        if (numbOfMiners > 12 && actualMiners < 20 && Utilities.spawnUnit(RobotType.MINER, rc))
        {
            return true;
        }
        else if (numbOfMiners > 12)
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
