package team044;

import battlecode.common.*;
import battlecode.world.Robot;

import java.util.Map;
import java.util.Random;

public class Utilities
{
    public static Random random = new Random();
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

    public static MapLocation greedyBestMiningSpot(RobotController rc) throws GameActionException
    {
        MapLocation best;
        MapLocation currentBest;

        best = rc.getLocation();
        Direction[] dirs = Direction.values();

        do
        {
            currentBest = best;
            MapLocation current = best;
            for (int i = 0; i < 8; i++)
            {
                MapLocation newSpot = current.add(dirs[i]);
                if (rc.canSenseLocation(newSpot))
                {
                    if (rc.senseOre(newSpot) > rc.senseOre(best) && !rc.isLocationOccupied(newSpot))
                    {
                        best = newSpot;
                    }
                    /*
                    for (int j = 0; j < 8; j++)
                    {
                        MapLocation newSpot2 = newSpot.add(dirs[j]);
                        if (rc.canSenseLocation(newSpot2))
                        {
                            if (rc.senseOre(newSpot2) > rc.senseOre(best) && !rc.isLocationOccupied(newSpot2))
                            {
                                best = newSpot2;
                            }
                        }
                    }*/
                }
            }
        } while (rc.senseOre(best) > rc.senseOre(currentBest));

        rc.setIndicatorString(1, "Best: "+currentBest);
        return currentBest;
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

        RobotInfo[] nearByAllies = rc.senseNearbyRobots(dist, rc.getTeam());
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
                        if (Clock.getBytecodeNum() > 4500)
                        {
                            break;
                        }
                        if (rc.isLocationOccupied(nearByAllies[i].location))
                        {
                            // transfer half of difference to them
                            int amount = (int) (rc.getSupplyLevel() - allySupply) / 2;
                            rc.transferSupplies(amount, nearByAllies[i].location);
                        }
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
                    if (Clock.getBytecodeNum() > 4000)
                    {
                        break;
                    }
                    if (rc.isLocationOccupied(nearByAllies[i].location))
                    {
                        rc.transferSupplies((totalSupplies/nearByAllies.length), nearByAllies[i].location);
                    }
                }
            }

            return true;
        }
        return false;
    }

    /**
     * This method is for a supply drone to give away its supply
     */
    public static boolean supplyArmy(RobotController rc) throws GameActionException
    {
        int dist = GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED - 1;

        RobotInfo[] allies = rc.senseNearbyRobots(dist, rc.getTeam());

        for (int i = 0; i < allies.length; i++)
        {
            if (Clock.getBytecodeNum() > 4000)
            {
                break;
            }
            // if building don't give it supply
            if (!allies[i].type.needsSupply())
            {
                continue;
            }
            int supplyAmount = (int) rc.getSupplyLevel() - 100;
            if (supplyAmount < 0 )
            {
                supplyAmount = 0;
            }
            rc.transferSupplies(supplyAmount, allies[i].location);
            return true;
        }

        return false;
    }

    /**
     * This method is for creating a structure
     * currently it tries to build it in the target location
     * but will build it in any open location if it can't
     */
    public static boolean BuildStructure(RobotController rc, MapLocation target, RobotType type) throws GameActionException
    {
        if (!rc.isCoreReady())
        {
            return false;
        }

        Direction dir = rc.getLocation().directionTo(target);

        if (rc.canMove(dir))
        {
            if (rc.canBuild(dir, type))
            {
                rc.build(dir, type);
                return true;
            }
        }
        else
        {
            Direction[] dirs = Direction.values();
            for (int i = 0; i < 8; i++)
            {
                if (rc.canMove(dirs[i]))
                {
                    if (rc.canBuild(dirs[i], type))
                    {
                        rc.build(dirs[i], type);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * This method returns the type of Building for an int
     * or -1 if it is not on list
     */
    public static RobotType getTypeForInt(int type)
    {
        if (type == BuildOrderMessaging.BuildAerospaceLab.ordinal())
        {
            return RobotType.AEROSPACELAB;
        }
        else if (type == BuildOrderMessaging.BuildBaracks.ordinal())
        {
            return RobotType.BARRACKS;
        }
        else if (type == BuildOrderMessaging.BuildHelipad.ordinal())
        {
            return RobotType.HELIPAD;
        }
        else if (type == BuildOrderMessaging.BuildMinerFactory.ordinal())
        {
            return RobotType.MINERFACTORY;
        }
        else if (type == BuildOrderMessaging.BuildTankFactory.ordinal())
        {
            return RobotType.TANKFACTORY;
        }
        else if (type == BuildOrderMessaging.BuildSupplyDepot.ordinal())
        {
            return RobotType.SUPPLYDEPOT;
        }
        else if (type == BuildOrderMessaging.BuildTechnologyInstitute.ordinal())
        {
            return RobotType.TECHNOLOGYINSTITUTE;
        }
        else if (type == BuildOrderMessaging.BuildTrainingField.ordinal())
        {
            return RobotType.TRAININGFIELD;
        }
        else if (type == BuildOrderMessaging.BuildMiningBaracks.ordinal())
        {
            return RobotType.BARRACKS;
        }
        return null;
    }

    /**
     * This method returns a MapLocation to build a target structure at
     */
    public static MapLocation findLocationForBuilding(RobotController rc, int numb, RobotType robotType) throws GameActionException
    {
        MapLocation target = null;

        // supply depot
        if (robotType == RobotType.SUPPLYDEPOT)
        {
            target = buildSupplyDepot(rc);
        }
        // mining facility
        else if (robotType == RobotType.MINERFACTORY)
        {
            target = buildMiningCamp(rc, numb);
        }
        // otherwise troop building
        else
        {
            target = buildTrainingFacility(rc);
        }

        return target;
    }

    /**
     * Currently and unimplemented method for determining where to put the next supply depot
     */
    public static MapLocation buildSupplyDepot(RobotController rc) throws GameActionException
    {
        MapLocation target = rc.senseHQLocation();

        // TODO: Implement supply depot creation currently is copy of building
        Direction[] dirs = Direction.values();

        random = new Random();

        int dir = random.nextInt(8);

        target = target.add(dirs[dir], 2);

        while (rc.isLocationOccupied(target))
        {
            dir = random.nextInt(8);

            target = target.add(dirs[dir], 2);
        }

        return target;
    }

    /**
     * This method builds a mining camp at the tower that the beaver was assigned to
     */
    public static MapLocation buildMiningCamp(RobotController rc, int numb)
    {
        MapLocation target = null;

        MapLocation[] towers = rc.senseTowerLocations();

        if (numb == 0)//< towers.length)
        {
            target = getTowerClosestToEnemyHQ(rc);
        }
        else
        {
            // first go to right
            if (numb == 1)//towers.length)
            {
                MapLocation ourHQ = rc.senseHQLocation();
                MapLocation enemyHQ = rc.senseEnemyHQLocation();
                int dist = ourHQ.distanceSquaredTo(enemyHQ);
                Direction dir = ourHQ.directionTo(enemyHQ);
                dir = dir.rotateRight();
                MapLocation current = rc.senseHQLocation().add(dir);
                int newDist = current.distanceSquaredTo(ourHQ);
                while (newDist < (dist/2) && newDist < 1000)
                {
                    current = current.add(dir);
                    newDist = current.distanceSquaredTo(ourHQ);
                    dir = current.directionTo(enemyHQ).rotateRight();
                }
                target = current;
            }
            // next go to left
            else
            {
                MapLocation ourHQ = rc.senseHQLocation();
                MapLocation enemyHQ = rc.senseEnemyHQLocation();
                int dist = ourHQ.distanceSquaredTo(enemyHQ);
                Direction dir = ourHQ.directionTo(enemyHQ);
                dir = dir.rotateLeft();
                MapLocation current = rc.senseHQLocation().add(dir);
                int newDist = current.distanceSquaredTo(ourHQ);
                while (newDist < dist/2 && newDist < 1000)
                {
                    current = current.add(dir);
                    newDist = current.distanceSquaredTo(ourHQ);
                    dir = current.directionTo(enemyHQ).rotateLeft();
                }
                target = current;
            }
        }

        target = target.add(target.directionTo(rc.getLocation()));

        return target;
    }

    /**
     * This method builds a training facility near our HQ
     * so that the units it spawns can get supply
     */
    public static MapLocation buildTrainingFacility(RobotController rc) throws GameActionException
    {
        MapLocation target = rc.senseHQLocation();

        Direction[] dirs = Direction.values();

        random = new Random();

        int dir = random.nextInt(8);

        target = target.add(dirs[dir], 2);

        while (rc.isLocationOccupied(target))
        {
            dir = random.nextInt(8);

            target = target.add(dirs[dir], 2);
        }

        return target;
    }

    /**
     * This method determines if we are within firing distance
     * of an enemy tower or HQ
     */
    public static boolean nearEnemyTower(RobotController rc)
    {
        MapLocation[] towers = rc.senseEnemyTowerLocations();

        for (int i = 0; i < towers.length; i++)
        {
            if (rc.getLocation().distanceSquaredTo(towers[i]) < 34)
            {
                return true;
            }
        }

        if (rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 34)
        {
            return true;
        }
        return false;
    }

    /**
     * This method returns the Tower that is closest to the enemy's HQ
     * Which is generally the first tower to come under attack
     */
    public static MapLocation getTowerClosestToEnemyHQ(RobotController rc)
    {
        MapLocation[] towers = rc.senseTowerLocations();
        MapLocation enemyHQ = rc.senseEnemyHQLocation();

        int bestDist = 9999999;
        MapLocation bestTower = null;

        for (int i = 0; i < towers.length; i++)
        {
            int dist = towers[i].distanceSquaredTo(enemyHQ);
            if (dist < bestDist)
            {
                bestDist = dist;
                bestTower = towers[i];
            }
        }

        return bestTower;
    }


    /**
     * This method handles units incrementing the counter that tells us how many
     * units of each type we have
     */
    public static void handleMessageCounter(RobotController rc, int channelOdd, int channelEven) throws GameActionException
    {
        // even
        if (Clock.getRoundNum() % 2 == 0)
        {
            int numb = rc.readBroadcast(channelEven);
            numb++;
            rc.broadcast(channelEven, numb);
        }
        // odd
        else
        {
            int numb = rc.readBroadcast(channelOdd);
            numb++;
            rc.broadcast(channelOdd, numb);
        }
    }

    /**
     * This method will build the requirement for a building
     */
    public static void buildRequirement(RobotController rc, MapLocation spot, RobotType type) throws GameActionException
    {
        // need to build barracks
        if (type == RobotType.TANKFACTORY)
        {
            BuildStructure(rc, spot, RobotType.BARRACKS);
        }
        // need to build a helipad
        else if (type == RobotType.AEROSPACELAB)
        {
            BuildStructure(rc, spot, RobotType.HELIPAD);
        }
        // need to build a technology institue
        else if (type == RobotType.TRAININGFIELD)
        {
            BuildStructure(rc, spot, RobotType.TECHNOLOGYINSTITUTE);
        }
        else
        {
            System.out.println("Unknown building type");
        }
    }

    /**
     * This method returns the closest tower
     */
    public static MapLocation closestTower(RobotController rc, MapLocation[] towers)
    {
        int closestDist = 99999;
        MapLocation closest = null;
        MapLocation us = rc.getLocation();

        for (int i = towers.length; --i>=0; )
        {
            int dist = towers[i].distanceSquaredTo(us);
            if (dist < closestDist)
            {
                closestDist = dist;
                closest = towers[i];
            }
        }

        return closest;
    }

    /**
     * This is a test
     */
    public static int test(RobotController rc) throws GameActionException
    {
        int numbOfMinerFactories = 0;
        int numbOfTankFactories = 0;
        int numbOfBarracks = 0;
        int numbOfHelipads = 0;
        int numbOfAerospacelab = 0;
        int numbOfSupplyDepots = 0;
        int numbOfTrainingfields = 0;
        int numbOfTechnologyInstitutes = 0;

        RobotInfo[] allies = rc.senseNearbyRobots(9999, rc.getTeam());

        for (int i = allies.length; --i>=0; )
        {
            if (allies[i].type == RobotType.BARRACKS)
            {
                numbOfBarracks++;
            }
            else if (allies[i].type == RobotType.MINERFACTORY)
            {
                numbOfMinerFactories++;
            }
            else if (allies[i].type == RobotType.TANKFACTORY)
            {
                numbOfTankFactories++;
            }
            else if (allies[i].type == RobotType.HELIPAD)
            {
                numbOfHelipads++;
            }
            else if (allies[i].type == RobotType.AEROSPACELAB)
            {
                numbOfAerospacelab++;
            }
            else if (allies[i].type == RobotType.SUPPLYDEPOT)
            {
                numbOfSupplyDepots++;
            }
            else if (allies[i].type == RobotType.TRAININGFIELD)
            {
                numbOfTrainingfields++;
            }
            else if (allies[i].type == RobotType.TECHNOLOGYINSTITUTE)
            {
                numbOfTechnologyInstitutes++;
            }

        }

        rc.setIndicatorString(0, "Barracks: " + numbOfBarracks + ", MinerFactory: " + numbOfMinerFactories + ", Tank Factory: " + ", Helipads: " + numbOfHelipads);
        return numbOfTankFactories + numbOfBarracks + numbOfHelipads + numbOfMinerFactories + numbOfAerospacelab + numbOfSupplyDepots + numbOfTechnologyInstitutes + numbOfTrainingfields;
    }

    /**
     * This method returns the rush location
     */
    public static MapLocation getRushLocation(RobotController rc)
    {
        MapLocation[] towers = rc.senseEnemyTowerLocations();

        if (towers.length == 0)
        {
            return rc.senseEnemyHQLocation();
        }

        int bestDist = 99999;
        MapLocation best = null;
        for (int i = towers.length; --i>=0; )
        {
            int dist = towers[i].distanceSquaredTo(rc.senseHQLocation());
            if (dist < bestDist)
            {
                bestDist = dist;
                best = towers[i];
            }
        }
        return best;
    }
}
