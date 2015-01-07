package theSimpleSoldier.Units;

import battlecode.common.*;
import battlecode.world.Util;
import theSimpleSoldier.Utilities;

public class MinerBeaver extends Beaver
{
    public MinerBeaver(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        // we need to move elsewhere
        if (rc.senseOre(rc.getLocation()) < 2)
        {
            target = Utilities.getBestMiningSpot(rc);
        }
    }
}
