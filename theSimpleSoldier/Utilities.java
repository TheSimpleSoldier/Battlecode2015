package theSimpleSoldier;

import battlecode.common.*;

import java.util.*;

public class Utilities
{
    public static Random random = new Random();
    // location for methods that can be used across multiple domains

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
                    }
                }
            }
        } while (rc.senseOre(best) > rc.senseOre(currentBest));

        if (currentBest == rc.getLocation())
        {
            Direction dir = dirs[random.nextInt(8)];
            while (!rc.canMove(dir))
            {
                dir = dirs[random.nextInt(8)];
            }
            currentBest = currentBest.add(dir);
        }

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
                    if (Clock.getBytecodeNum() > 4500)
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
    public static MapLocation buildSupplyDepot(RobotController rc)
    {
        MapLocation target = null;

        // TODO: Implement supply depot creation

        return target;
    }

    /**
     * This method builds a mining camp at the tower that the beaver was assigned to
     */
    public static MapLocation buildMiningCamp(RobotController rc, int numb)
    {
        MapLocation target = null;

        MapLocation[] towers = rc.senseTowerLocations();

        if (numb < towers.length)
        {
            target = towers[numb];
        }
        else
        {
            // first go to right
            if (numb == towers.length)
            {
                MapLocation ourHQ = rc.senseHQLocation();
                int dist = ourHQ.distanceSquaredTo(rc.senseEnemyHQLocation());
                Direction dir = ourHQ.directionTo(rc.senseEnemyHQLocation());
                dir = dir.rotateRight();
                MapLocation current = rc.senseHQLocation().add(dir);
                int newDist = current.distanceSquaredTo(ourHQ);
                while (newDist < (dist/2))
                {
                    current = current.add(dir);
                    newDist = current.distanceSquaredTo(ourHQ);
                }
                target = current;
            }
            // next go to left
            else
            {
                MapLocation ourHQ = rc.senseHQLocation();
                int dist = ourHQ.distanceSquaredTo(rc.senseEnemyHQLocation());
                Direction dir = ourHQ.directionTo(rc.senseEnemyHQLocation());
                dir = dir.rotateLeft();
                MapLocation current = rc.senseHQLocation().add(dir);
                int newDist = current.distanceSquaredTo(ourHQ);
                while (newDist < dist/2)
                {
                    current = current.add(dir);
                    newDist = current.distanceSquaredTo(ourHQ);
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
}