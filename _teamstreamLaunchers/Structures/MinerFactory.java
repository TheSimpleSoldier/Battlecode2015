package _teamstreamLaunchers.Structures;

import battlecode.common.*;
import _teamstreamLaunchers.Messaging;
import _teamstreamLaunchers.Structure;
import _teamstreamLaunchers.Utilities;

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
        else if (numbOfMiners > maxWorkers)
        {
            return false;
        }
        if (Clock.getRoundNum() > rc.getRoundLimit() - 500){
            return false;
        }

        int mapSize = rc.readBroadcast(Messaging.MapSize.ordinal());
        if (mapSize == 0)
            mapSize = ourHQ.distanceSquaredTo(enemyHQ);
        int bestOre = rc.readBroadcast(Messaging.BestOre.ordinal());
        switch (actualMiners/5)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                if (Utilities.spawnUnit(RobotType.MINER,rc)) {
                    return true;
                }
                break;
            case 5:
                if (mapSize > 1600 && bestOre > 9 && Utilities.spawnUnit(RobotType.MINER,rc)){
                    return true;
                }
                break;
            case 6:
                if (mapSize > 2500 && bestOre > 9 && Utilities.spawnUnit(RobotType.MINER,rc)){
                    return true;
                }
                break;
            case 7:
            case 8:
                if (mapSize > 3600 && bestOre > 9 && Utilities.spawnUnit(RobotType.MINER,rc)){
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }
}
