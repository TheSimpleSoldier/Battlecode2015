package team044.Units;

import battlecode.common.*;
import team044.BuildOrderMessaging;
import team044.Messaging;
import team044.Unit;
import team044.Utilities;

import java.util.Random;

public class BuildingBeaver extends Beaver
{
    MapLocation nextBuildSpot;
    Boolean build;
    RobotType building = null;
    RobotType building2 = null;
    RobotType building3 = null;
    Direction dir;
    static Random rand;
    Direction[] dirs;
    boolean becomeMiner;
    int numb;
    MapLocation buildingSpot;
    int type;
    boolean foundSpot;

    public BuildingBeaver(RobotController rc) throws GameActionException
    {
        super(rc);
        //rc.setIndicatorString(1, "BuildingBeaver");
        build = false;
        dirs = Direction.values();

        becomeMiner = false;
        numb = rc.readBroadcast(Messaging.NumbOfFactories.ordinal());
        rand = new Random(rc.getID());
        type = -1;
        foundSpot = false;
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        if(type == BuildOrderMessaging.BuildMiningBaracks.ordinal() || type == BuildOrderMessaging.BuildMiningAeroSpaceLab.ordinal() && !foundSpot)
        {
            MapLocation temp = Utilities.getBestSpotSimple(rc);
            if(!temp.equals(rc.getLocation()))
            {
                rc.setIndicatorString(1, "Building in new spot: " + temp);
                buildingSpot = temp;
                target = buildingSpot.add(buildingSpot.directionTo(rc.getLocation()));
                foundSpot = true;
            }
        }

        if (building == null && rc.isCoreReady())
        {
            type = rc.readBroadcast(Messaging.BuildOrder.ordinal());

            building = Utilities.getTypeForInt(type);

            rc.setIndicatorString(2, "Building: " + building + ", target: " + target);

            if (building == null && Clock.getRoundNum() > 500)
            {
                building = RobotType.SUPPLYDEPOT;
            }

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
                building3 = RobotType.TANKFACTORY;
                numb = rc.readBroadcast(Messaging.NumbOfFactories.ordinal());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                rc.broadcast(Messaging.NumbOfFactories.ordinal(), (numb+1));
                target = Utilities.findLocationForBuilding(rc, numb, building);
                buildingSpot = target;
                target = target.add(target.directionTo(rc.getLocation()));
                rc.setIndicatorString(0, "Numb: " + numb);
                rc.setIndicatorString(2, "Building: " + building + ", Building Spot" + buildingSpot);
            }
            else if (type == BuildOrderMessaging.BuildMiningAeroSpaceLab.ordinal())
            {
                rc.setIndicatorString(1, "Build Mining AerospaceLab");
                building = RobotType.MINERFACTORY;
                building2 = RobotType.HELIPAD;
                building3 = RobotType.AEROSPACELAB;
                numb = rc.readBroadcast(Messaging.NumbOfFactories.ordinal());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                rc.broadcast(Messaging.NumbOfFactories.ordinal(), (numb+1));
                target = Utilities.findLocationForBuilding(rc, numb, building);
                buildingSpot = target;
                target = target.add(target.directionTo(rc.getLocation()));
                rc.setIndicatorString(0, "Numb: " + numb);
                rc.setIndicatorString(2, "Building: " + building + ", Building Spot" + buildingSpot);
            }
            else
            {
                rc.setIndicatorString(0, " In else: " + target);
                numb = rc.readBroadcast(Messaging.NumbOfFactories.ordinal());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                if (building == RobotType.MINERFACTORY)
                {
                    rc.broadcast(Messaging.NumbOfFactories.ordinal(), (numb+1));
                }

                target = Utilities.findLocationForBuilding(rc, numb, building);


                if (target == null)
                {
                    rc.setIndicatorString(1, "Target was null");
                    target = rc.getLocation().add(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
                }

                buildingSpot = target;
                target = target.add(target.directionTo(rc.getLocation()));
                rc.setIndicatorString(0, "Numb: " + numb);
                rc.setIndicatorString(2, "Building: " + building + ", Building Spot" + buildingSpot + ", target: " + target);
            }
        }

        if (target != null && rc.canSenseLocation(target) && rc.getLocation().distanceSquaredTo(target) < 24)
        {
            rc.setIndicatorString(1, "can sense Spot");
            if (rc.senseTerrainTile(target) == TerrainTile.OFF_MAP || rc.senseTerrainTile(target) == TerrainTile.VOID || rc.isLocationOccupied(target))
            {
                if (type != BuildOrderMessaging.BuildMiningBaracks.ordinal() && type != BuildOrderMessaging.BuildMiningAeroSpaceLab.ordinal())
                {
                    buildingSpot = Utilities.findLocationForBuilding(rc, numb, building);
                    target = buildingSpot.add(buildingSpot.directionTo(rc.getLocation()));
                }
                else
                {
                    //rc.setIndicatorString(2, "build = true");
                    target = rc.getLocation();
                    build = true;
                }
            }
        }

        if (type != BuildOrderMessaging.BuildMiningBaracks.ordinal() && type != BuildOrderMessaging.BuildMiningAeroSpaceLab.ordinal() && target != null && buildingSpot != null && rc.canSenseLocation(buildingSpot) && rc.isLocationOccupied(buildingSpot))
        {
            buildingSpot = Utilities.findLocationForBuilding(rc, numb, building);
            rc.setIndicatorString(2, "New Building Spot: " + buildingSpot + ", old Target");
            target = buildingSpot.add(buildingSpot.directionTo(rc.getLocation()));
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

        if (build || rc.getLocation().distanceSquaredTo(buildingSpot) < 3)
        {
            if (Utilities.BuildStructure(rc, buildingSpot, building))
            {
                if (building3 != null)
                {
                    building = building2;
                    building2 = building3;
                    building3 = null;
                    return true;
                }
                else if (building2 != null)
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
