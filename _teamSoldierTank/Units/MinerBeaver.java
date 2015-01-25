package _teamSoldierTank.Units;

import battlecode.common.*;
import _teamSoldierTank.Utilities;

import java.util.Random;

public class MinerBeaver extends Beaver
{
    static Random rand;
    public MinerBeaver(RobotController rc)
    {
        super(rc);
        rc.setIndicatorString(1, "Miner Beaver");
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        // we need to move elsewhere
        if (rc.senseOre(rc.getLocation()) < 5)
        {
            //target = Utilities.getBestMiningSpot(rc);
            target = Utilities.greedyBestMiningSpot(rc);
        }
    }
}
