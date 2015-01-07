package theSimpleSoldier.Units;

import battlecode.common.*;
import theSimpleSoldier.BuildOrderMessaging;
import theSimpleSoldier.Messaging;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Utilities;
import theSimpleSoldier.Unit;

import java.util.Random;

public class BuildingBeaver extends Beaver
{
    MapLocation nextBuildSpot;
    Boolean build;
    RobotType building;
    Direction dir;
    static Random rand;
    Direction[] dirs;
    boolean becomeMiner;
    int numb;

    public BuildingBeaver(RobotController rc) throws GameActionException
    {
        super(rc);
        build = false;
        dirs = Direction.values();
        target = rc.senseTowerLocations()[0];
        becomeMiner = false;
        numb = rc.readBroadcast(Messaging.NumbOfBeavers.ordinal());
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        if (building == null)
        {
            int type = rc.readBroadcast(Messaging.BuildOrder.ordinal());

            building = Utilities.getTypeForInt(type);

            if (type == BuildOrderMessaging.DoneBuilding.ordinal())
            {
                becomeMiner = true;
            }
            else if (building == null)
            {
                // just mine until we get a different job
                if (rc.senseOre(rc.getLocation()) < 5)
                {
                    //target = Utilities.getBestMiningSpot(rc);
                    target = Utilities.greedyBestMiningSpot(rc);
                }
            }
            else
            {
                target = Utilities.findLocationForBuilding(rc, numb, building);
            }
        }
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (!rc.isCoreReady())
        {
            return false;
        }

        if (target == null || building == null)
        {
            if (rc.canMine() && rc.senseOre(rc.getLocation()) >= 2)
            {
                rc.mine();
                return true;
            }
            return false;
        }

        if (rc.getLocation().isAdjacentTo(target))
        {
            if (Utilities.BuildStructure(rc, target, building))
            {
                target = null;
                building = null;
            }
        }

        return false;
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (becomeMiner)
        {
            return new MinerBeaver(rc);
        }
        return current;
    }
}
