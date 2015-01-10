package team044.Units;

import battlecode.common.*;
import team044.BuildOrderMessaging;
import team044.Messaging;
import team044.Utilities;
import team044.Unit;

import java.awt.*;
import java.util.Random;

public class BuildingBeaver extends Beaver
{
    MapLocation nextBuildSpot;
    Boolean build;
    RobotType building = null;
    RobotType building2 = null;
    Direction dir;
    static Random rand;
    Direction[] dirs;
    boolean becomeMiner;
    int numb;
    MapLocation buildingSpot;

    public BuildingBeaver(RobotController rc) throws GameActionException
    {
        super(rc);
        //rc.setIndicatorString(1, "BuildingBeaver");
        build = false;
        dirs = Direction.values();

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
            else if (type == BuildOrderMessaging.BuildMiningBaracks.ordinal())
            {
                rc.setIndicatorString(1, "Build Mining Baracks");
                building = RobotType.MINERFACTORY;
                building2 = RobotType.BARRACKS;
                numb = rc.readBroadcast(Messaging.NumbOfBeavers.ordinal());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                target = Utilities.findLocationForBuilding(rc, numb, building);
                buildingSpot = target;
                target = target.add(target.directionTo(rc.getLocation()));
                rc.setIndicatorString(0, "Numb: " + numb);
                rc.setIndicatorString(2, "Building: " + building + ", Building Spot" + buildingSpot);
            }
            else
            {
                numb = rc.readBroadcast(Messaging.NumbOfBeavers.ordinal());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                target = Utilities.findLocationForBuilding(rc, numb, building);
                buildingSpot = target;
                target = target.add(target.directionTo(rc.getLocation()));
                //rc.setIndicatorString(0, "Numb: " + numb);
                //rc.setIndicatorString(2, "Building: " + building + ", Building Spot" + buildingSpot);
            }
        }

        if (target != null && rc.canSenseLocation(target) && rc.getLocation().distanceSquaredTo(target) < 24)
        {
            rc.setIndicatorString(1, "can sense Spot");
            if (rc.senseTerrainTile(target) == TerrainTile.OFF_MAP || rc.senseTerrainTile(target) == TerrainTile.VOID)
            {
                rc.setIndicatorString(1, "build = true");
                target = rc.getLocation();
                build = true;
            }
        }
    }

    public boolean fight() throws GameActionException
    {
        return false;
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

        if (build || rc.getLocation().distanceSquaredTo(buildingSpot) < 15)
        {
            if (Utilities.BuildStructure(rc, buildingSpot, building))
            {
                if (building2 != null)
                {
                    building = building2;
                    building2 = null;
                    return true;
                }
                else
                {
                    target = null;
                    building = null;
                    build = false;
                    return true;
                }
            }
            // if we don't have a requirement then build it.
            else if (rc.getTeamOre() > building.oreCost && !rc.hasBuildRequirements(building))
            {
                rc.setIndicatorString(1, "Building requirement");
                Utilities.buildRequirement(rc, buildingSpot, building);
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
