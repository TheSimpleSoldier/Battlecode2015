package _teamfightMicro.Units;

import battlecode.common.*;
import _teamfightMicro.BuildOrderMessaging;
import _teamfightMicro.Messaging;
import _teamfightMicro.Utilities;
import _teamfightMicro.Unit;

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
            else if (type == BuildOrderMessaging.BuildMiningBaracks.ordinal())
            {
                RobotInfo[] allies = rc.senseNearbyRobots(24, rc.getTeam());
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                target = null;
                for (int i = 0; i < allies.length; i++)
                {
                    if (allies[i].type == RobotType.MINERFACTORY)
                    {
                        target = allies[i].location;
                        break;
                    }
                }

                if (target != null)
                {
                    target = target.add(target.directionTo(rc.getLocation()));
                }
                else
                {
                    building = null;
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

        if (rc.getLocation().distanceSquaredTo(buildingSpot) < 15)
        {
            if (Utilities.BuildStructure(rc, buildingSpot, building))
            {
                target = null;
                building = null;
                return true;
            }
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
