package theSimpleSoldier;

import battlecode.common.*;

import java.util.Stack;

public class Utilities
{
    // location for methods that can be used across multiple domains

    /**
     * This method returns the location in the Robots sensor range
     * with the highest ore
     */
    public static MapLocation getBestMiningSpot(RobotController rc)
    {
        int range = rc.getType().sensorRadiusSquared;
        MapLocation ourLocation  = rc.getLocation();
        MapLocation current = ourLocation;
        MapLocation best = ourLocation;
        Stack<MapLocation> stack = new Stack<MapLocation>();
        Stack<MapLocation> stack2 = new Stack<MapLocation>();
        stack2.push(current);
        Direction[] dirs = Direction.values();

        do
        {
            current = stack2.pop();
            MapLocation next;
            for (int i = 0; i < dirs.length; i++)
            {
                next = current.add(dirs[i]);

                if (!stack.contains(next)) {
                    if (ourLocation.distanceSquaredTo(next) < range)
                    {
                        stack2.push(next);

                        if (rc.senseOre(next) > rc.senseOre(best))
                        {
                            best = next;
                        }
                    }
                }
            }
        } while (!stack2.empty());

        return best;
    }

    /**
     * This function returns the Robot type for a given message
     */
    public static RobotType getRobotType(BuildOrderMessaging message)
    {
        switch(message)
        {
            case BuildBeaverMiner:
                return RobotType.BEAVER;
            case BuildBeaverBuilder:
                return RobotType.BEAVER;
            case BuildAerospaceLab:
                return RobotType.AEROSPACELAB;
            case BuildBaracks:
                return RobotType.BARRACKS;
            case BuildBasher:
                return RobotType.BASHER;
            case BuildCommander:
                return RobotType.COMMANDER;
            case BuildComputer:
                return RobotType.COMPUTER;
            case BuildDrone:
                return RobotType.DRONE;
            case BuildHelipad:
                return RobotType.HELIPAD;
            case BuildLauncher:
                return RobotType.LAUNCHER;
            case BuildMiner:
                return RobotType.MINER;
            case BuildMinerFactory:
                return RobotType.MINERFACTORY;
            case BuildSoldier:
                return RobotType.SOLDIER;
            case BuildTank:
                return RobotType.TANK;
            case BuildTankFactory:
                return RobotType.TANKFACTORY;
            case BuildTechnologyInstitute:
                return RobotType.TECHNOLOGYINSTITUTE;
            case BuildTrainingField:
                return RobotType.TRAININGFIELD;
            case BuildSupplyDepot:
                return RobotType.SUPPLYDEPOT;
        }

        return null;
    }

    /**
     * This method determines if a unit can build another robot
     */
    public static boolean canBuild(RobotType robot, RobotController rc)
    {
        Direction[] dirs = Direction.values();

        for (int i = 0; i < dirs.length; i++)
        {
            if (rc.canMove(dirs[i]))
            {
                if (rc.canBuild(dirs[i], robot))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * This method handles all of the spawning of units
     */
    public static boolean spawnUnit(RobotType type, RobotController rc) throws GameActionException
    {
        if (!rc.isCoreReady())
        {
            return false;
        }

        if (type.oreCost > rc.getTeamOre())
        {
            return false;
        }

        Direction[] dirs = Direction.values();
        for (int i = 0; i < 8; i++)
        {
            if (rc.canSpawn(dirs[i], type))
            {
                rc.spawn(dirs[i], type);
                return true;
            }
        }
        return false;
    }
}
