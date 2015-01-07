package theSimpleSoldier;

import battlecode.common.*;

import java.util.Random;
import java.util.Stack;

public class Utilities
{
    // location for methods that can be used across multiple domains

    private static int startChannelMineSpots = 100;

    public static MapLocation getBestSpot(RobotController rc, boolean lightWeight) throws GameActionException
    {
        int numberMineSpots = 3;

        MapLocation location = rc.senseHQLocation();
        Random rand = new Random();

        //set to 20 or 50 depending on how many bytecodes you want to use
        //20 will use about 1700 and 50 will use about 4500
        int k = 50;
        if(lightWeight)
        {
            k = 20;
        }

        //run hill climbing for how many iterations specified earlier.
        for(; --k >= 0;)
        {
            MapLocation nextLocation = location.add(Direction.values()[rand.nextInt(8)]);

            if(rc.senseOre(nextLocation) >= rc.senseOre(location))
            {
                location = nextLocation;
            }
        }

        if(!minesInitialized(rc, numberMineSpots))
        {
            initializeMines(rc, numberMineSpots);
        }

        int startChannel = -1;

        //if far from us, but also not near enemy
        if(farFromHome(rc, rc.getTeam(), location) && farFromHome(rc, rc.getTeam().opponent(), location))
        {
            startChannel = startChannelMineSpots + numberMineSpots * 3;
        }
        //if near us
        else if(!farFromHome(rc, rc.getTeam(), location))
        {
            startChannel = startChannelMineSpots;
        }

        //this looks at the current top spots and inserts the current spot if it is larger big enough
        if(startChannel != -1)
        {
            for(int a = 0; a < numberMineSpots; a++)
            {
                if(rc.readBroadcast(startChannel + a * 3) == location.x &&
                   rc.readBroadcast(startChannel + a * 3 + 1) == location.y &&
                   rc.readBroadcast(startChannel + a * 3 + 2) == rc.senseOre(location))
                {
                    return location;
                }
            }
            int lastBiggest = -1;
            for(int a = 0; a < numberMineSpots; a++)
            {
                if(rc.readBroadcast(startChannel + a * 3 + 2) < rc.senseOre(location))
                {
                    lastBiggest = a;
                    //shuffle order of locations to make room for new one
                    if(a > 0)
                    {
                        rc.broadcast(startChannel + (a - 1) * 3, rc.readBroadcast(startChannel + a * 3));
                        rc.broadcast(startChannel + (a - 1) * 3 + 1, rc.readBroadcast(startChannel + a * 3 + 1));
                        rc.broadcast(startChannel + (a - 1) * 3 + 2, rc.readBroadcast(startChannel + a * 3 + 2));
                    }
                }
            }
            if(lastBiggest > -1)
            {
                rc.broadcast(startChannel + lastBiggest * 3, location.x);
                rc.broadcast(startChannel + lastBiggest * 3 + 1, location.y);
                rc.broadcast(startChannel + lastBiggest * 3 + 2, (int)Math.round(rc.senseOre(location)));
            }
        }

        //this is mainly for debug purposes and should be removed when it no longer needs to be checked
        return location;
    }

    //sets all ore values to -1
    private static void initializeMines(RobotController rc, int numberMineSpots) throws GameActionException
    {
        for(int k = startChannelMineSpots + 2; k < startChannelMineSpots + numberMineSpots * 2 * 3; k += 3)
        {
            rc.broadcast(k, -1);
        }
    }

    //this will check if a spot is near the towers or hq of a particular team.
    private static boolean farFromHome(RobotController rc, Team team, MapLocation location)
    {
        int close = 10;

        boolean opponent = false;
        if(rc.getTeam() != team)
        {
            opponent = true;
        }

        if(opponent)
        {
            if(location.distanceSquaredTo(rc.senseEnemyHQLocation()) < close)
            {
                return false;
            }
        }
        else
        {
            if(location.distanceSquaredTo(rc.senseHQLocation()) < close)
            {
                return false;
            }
        }

        MapLocation[] towers;
        if(opponent)
        {
            towers = rc.senseEnemyTowerLocations();
        }
        else
        {
            towers = rc.senseTowerLocations();
        }

        for(int k = towers.length; --k >= 0;)
        {
            if(location.distanceSquaredTo(towers[k]) < close)
            {
                return false;
            }
        }

        return true;
    }

    //checks if the mines channels have been initialized yet
    private static boolean minesInitialized(RobotController rc, int numberMineSpots) throws GameActionException
    {
        for(int k = startChannelMineSpots; k < startChannelMineSpots + numberMineSpots * 2 * 3; k++)
        {
            if(rc.readBroadcast(k) != 0)
            {
                return true;
            }
        }

        return false;
    }

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
            if (Clock.getBytecodeNum() > 4000 && rc.senseOre(best) >= 2)
            {
                return best;
            }
            current = stack2.pop();
            MapLocation next;
            for (int i = 0; i < dirs.length; i++)
            {
                next = current.add(dirs[i]);

                 if (!stack.contains(next))
                 {
                    if (ourLocation.distanceSquaredTo(next) < range)
                    {
                        stack2.push(next);

                        if (rc.senseOre(next) > rc.senseOre(best))
                        {
                            best = next;
                        }
                    }
                    stack.push(next);
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

    /**
     * This method is for a unit to distribute Supplies to allies who are further away from the HQ
     */
    public static void shareSupplies(RobotController rc) throws GameActionException
    {
        int dist = GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED - 1;

        RobotInfo[] nearByAllies = rc.senseNearbyRobots(dist, rc.getTeam().opponent());
        if (nearByAllies.length <= 0)
        {
            return;
        }

        MapLocation ourHQ = rc.senseHQLocation();
        int distToHQ = rc.getLocation().distanceSquaredTo(ourHQ);

        if (shareAllSupplies(rc, nearByAllies))
        {
        }
        else
        {
            for (int i = 0; i < nearByAllies.length; i++)
            {
                int allyDist = nearByAllies[i].location.distanceSquaredTo(ourHQ);
                if (allyDist > distToHQ)
                {
                    int allySupply = (int) nearByAllies[i].supplyLevel;
                    if (allySupply < rc.getSupplyLevel())
                    {
                        // transfer half of difference to them
                        int amount = (int) (rc.getSupplyLevel() - allySupply) / 2;
                        rc.transferSupplies(amount, nearByAllies[i].location);
                    }
                }
            }
        }
    }
    
    /**
     * This method is for transferring almost all supplies right before death
     */
    public static boolean shareAllSupplies(RobotController rc, RobotInfo[] nearByAllies) throws GameActionException
    {
        if (rc.getHealth() < 20)
        {
            int totalSupplies = (int) rc.getSupplyLevel();
            totalSupplies = totalSupplies - 50;

            if (totalSupplies > 0)
            {
                for (int i = 0; i < nearByAllies.length; i++)
                {
                    rc.transferSupplies((totalSupplies/nearByAllies.length), nearByAllies[i].location);
                }
            }

            return true;
        }
        return false;
    }
}
