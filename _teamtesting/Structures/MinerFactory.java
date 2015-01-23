package _teamtesting.Structures;

import battlecode.common.*;
import _teamtesting.Messaging;
import _teamtesting.Structure;
import _teamtesting.Utilities;

public class MinerFactory extends Structure
{
    private int numbOfMiners;
    private int actualMiners = 0;
    private int maxWorkers = 20;

    public MinerFactory(RobotController rc)
    {
        super(rc);
        maxWorkers = 20;
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
        else if (numbOfMiners > maxWorkers && actualMiners < 30 && Utilities.spawnUnit(RobotType.MINER, rc) && Clock.getRoundNum() > 750)
        {
            return true;
        }
        else if (numbOfMiners > maxWorkers)
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
