package theSimpleSoldier.Units;

import battlecode.common.*;
import theSimpleSoldier.BuildOrderMessaging;
import theSimpleSoldier.Messaging;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Utilities;

import java.util.Random;

public class BuildingBeaver extends Beaver
{
    MapLocation nextBuildSpot;
    Boolean build;
    RobotType building;
    Direction dir;
    static Random rand;
    Direction[] dirs;
    public BuildingBeaver(RobotController rc)
    {
        super(rc);
        build = false;
        dirs = Direction.values();
        target = rc.senseTowerLocations()[0];
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        if (!build)
        {
            BuildOrderMessaging message = BuildOrderMessaging.values()[buildingType];
            RobotType robot = Utilities.getRobotType(message);
            if (Utilities.canBuild(robot, rc))
            {
                build = true;
                building = robot;
                rc.broadcast(Messaging.BuildOrder.ordinal(), -1);
                MapLocation[] towers = rc.senseTowerLocations();
                int random = rand.nextInt(towers.length);
                target = towers[random].add(rc.getLocation().directionTo(towers[random]).opposite());
            }
        }
        dir = rc.getLocation().directionTo(target);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (rc.isCoreReady() && build && rc.canBuild(dir, building) && rc.getLocation().isAdjacentTo(target))
        {
            rc.build(dir, building);
            return true;
        }

        while (rc.getLocation().isAdjacentTo(target) && !rc.canBuild(dir, building))
        {
            target = target.add(dirs[rand.nextInt(8)]);
        }

        return false;
    }

}
