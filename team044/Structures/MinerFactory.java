package team044.Structures;

import battlecode.common.*;
import team044.Messaging;
import team044.Structure;
import team044.Utilities;

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
        int mapSize = rc.readBroadcast(Messaging.MapSize.ordinal());
        int bestOre = rc.readBroadcast(Messaging.BestOre.ordinal());
        switch (actualMiners/5)
        {
            case 0:
            case 1:
            case 2:
                if (Utilities.spawnUnit(RobotType.MINER,rc))
                    return true;
                break;
            case 3:
                if (mapSize > 1600 && bestOre > 10 && Utilities.spawnUnit(RobotType.MINER,rc))
                    return true;
                break;
            case 4:
                if (mapSize > 2000 && bestOre > 10 && Utilities.spawnUnit(RobotType.MINER,rc))
                    return true;
                break;
            case 5:
                if (mapSize > 2500 && bestOre > 10 && Utilities.spawnUnit(RobotType.MINER,rc))
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }
}
