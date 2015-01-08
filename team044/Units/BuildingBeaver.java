package team044.Units;

import battlecode.common.*;
import team044.BuildOrderMessaging;
import team044.Messaging;
import team044.Utilities;
import team044.Unit;

import java.util.Random;

public class BuildingBeaver extends Beaver
{
    MapLocation nextBuildSpot;
    Boolean build;
    RobotType building = null;
    Direction dir;
    static Random rand;
    Direction[] dirs;
    boolean becomeMiner;
    int numb;
    MapLocation buildingSpot;

    public BuildingBeaver(RobotController rc) throws GameActionException
    {
        super(rc);
        rc.broadcast(BuildOrderMessaging.BuilderAlive.ordinal(), 1);  // BuilderAlive = 1 next round, indicating live builder beaver to HQ
        rc.setIndicatorString(1, "BuildingBeaver");
        build = false;
        dirs = Direction.values();
        target = rc.senseTowerLocations()[0];
        becomeMiner = false;
        numb = rc.readBroadcast(Messaging.NumbOfBeavers.ordinal());
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        if (building == null && rc.isCoreReady())
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
                numb = rc.readBroadcast(Messaging.NumbOfBeavers.ordinal());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                target = Utilities.findLocationForBuilding(rc, numb, building);
                buildingSpot = target;
                target = target.add(target.directionTo(rc.getLocation()));
                rc.setIndicatorString(0, "Numb: " + numb);
                rc.setIndicatorString(2, "Building: " + building + ", Building Spot" + buildingSpot);
            }
        }
    }

    public boolean carryOutAbility() throws GameActionException
    {
        rc.broadcast(BuildOrderMessaging.BuilderAlive.ordinal(), 1);  // BuilderAlive = 1 next round, indicating live builder beaver to HQ
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

        if (rc.getLocation().distanceSquaredTo(buildingSpot) < 15)
        {
            if (Utilities.BuildStructure(rc, buildingSpot, building))
            {
                target = null;
                building = null;
                return true;
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
