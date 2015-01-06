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
    boolean builtTankFactory = false;
    boolean builtBaracks = false;

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
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (!rc.isCoreReady())
        {
            return false;
        }

        if (!rc.getLocation().isAdjacentTo(rc.senseHQLocation()))
        {
            Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
            if (builtBaracks)
            {
                while(!rc.canBuild(dir, RobotType.TANKFACTORY))
                {
                    dir = dir.rotateRight();
                }
                rc.build(dir, RobotType.TANKFACTORY);
                if (rc.hasBuildRequirements(RobotType.TANKFACTORY))
                {
                    builtTankFactory = true;
                }
            }
            else
            {
                while(!rc.canBuild(dir, RobotType.BARRACKS))
                {
                    dir = dir.rotateRight();
                }
                rc.build(dir, RobotType.BARRACKS);
                builtBaracks = true;
            }
            return true;
        }

        return false;
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (builtTankFactory)
        {
            return new MinerBeaver(rc);
        }
        return current;
    }
}
