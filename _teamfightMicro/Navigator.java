package _teamfightMicro;

import battlecode.common.*;

import java.util.Random;

public class Navigator
{
    private RobotController rc;
    private MapLocation dog, target;
    private Random rand;
    private boolean goingLeft, goingAround;
    private Direction lastFacing;

    public Navigator(RobotController rc)
    {
        this.rc = rc;
        dog = rc.getLocation();
        target = rc.getLocation();
        rand = new Random(rc.getID());
        goingLeft = rand.nextBoolean();
        goingAround = false;
        lastFacing = Direction.NONE;
    }

    public boolean takeNextStep(MapLocation target) throws GameActionException
    {
        return takeNextStep(target, false, false);
    }

    public boolean takeNextStep(MapLocation target, boolean avoidTowers) throws GameActionException
    {
        return takeNextStep(target, avoidTowers, false);
    }

    //This method uses a walk the dog method
    //The walk the dog method has a dog that runs away till the owner
    //is about to lose sight of it, then it sits there until the owner catches up.
    //The dog moves in a bug pattern, but the owner will cut corners.
    public boolean takeNextStep(MapLocation target, boolean avoidTowers, boolean isDrone) throws GameActionException
    {
        //if target changed, act like dog is next to owner
        if(!target.equals(this.target))
        {
            dog = rc.getLocation();
            this.target = target;
        }

        //if dog is at owner's location, it runs to its next spot
        dogGo(avoidTowers, isDrone);

        //if you can move towards the dog, do
        if(rc.canMove(rc.getLocation().directionTo(dog)) && rc.isCoreReady())
        {
            rc.move(rc.getLocation().directionTo(dog));
            return true;
        }
        //otherwise, if you can move, something is in the way, so reroute
        else if(rc.isCoreReady())
        {
            dog = rc.getLocation();
        }

        return false;
    }

    //This is the method that moves the dog along till it is almost out of sight
    private void dogGo(boolean avoidTowers, boolean isDrone) throws GameActionException
    {
        Direction lastDir = Direction.NONE;
        int count = 0;
        //go till out of site
        while(dogInSight(dog, avoidTowers, isDrone) && !dog.equals(target))
        {
            if(count > Constants.maxDogSteps)
            {
                return;
            }
            count++;
            //This is used so the dog knows if it is going around an object
            //prevents bugging around exterior of map
            if(goingAround)
            {
                Direction towardTarget = dog.directionTo(target);
                if(goingLeft)
                {
                    if(rc.senseTerrainTile(dog.add(lastFacing.rotateRight().rotateRight())) == TerrainTile.OFF_MAP &&
                       lastFacing != towardTarget && lastFacing.rotateRight() != towardTarget &&
                       lastFacing.rotateLeft() != towardTarget)
                    {
                        goingLeft = !goingLeft;
                        lastFacing = lastFacing.rotateRight().rotateRight().rotateRight().rotateRight();
                    }
                }
                else
                {
                    if(rc.senseTerrainTile(dog.add(lastFacing.rotateLeft().rotateLeft())) == TerrainTile.OFF_MAP &&
                       lastFacing != towardTarget && lastFacing.rotateRight() != towardTarget &&
                       lastFacing.rotateLeft() != towardTarget &&
                       lastFacing.rotateRight().rotateRight() != towardTarget &&
                       lastFacing.rotateLeft().rotateLeft() != towardTarget)
                    {
                        goingLeft = !goingLeft;
                        lastFacing = lastFacing.rotateRight().rotateRight().rotateRight().rotateRight();
                    }
                }
            }

            //preventing it getting stuck in bowls
            if(goingAround)
            {
                if(goingLeft)
                {
                    lastDir = lastFacing.rotateRight();
                }
                else
                {
                    lastDir = lastFacing.rotateLeft();
                }
            }
            else
            {
                lastDir = dog.directionTo(target);
            }

            MapLocation nextSpot = dog.add(lastDir);

            boolean nearTower = false;
            if(avoidTowers)
            {
                MapLocation[] towers = rc.senseEnemyTowerLocations();
                for(int k = 0; k < towers.length; k++)
                {
                    if(nextSpot.distanceSquaredTo(towers[k]) <= 24)
                    {
                        nearTower = true;
                    }
                }
                if(nextSpot.distanceSquaredTo(rc.senseEnemyHQLocation()) <= 24)
                {
                    nearTower = true;
                }
            }

            if(!isDrone)
            {
                if((rc.canSenseLocation(nextSpot) &&
                    !rc.senseTerrainTile(nextSpot).isTraversable() &&
                    rc.senseTerrainTile(nextSpot) != TerrainTile.UNKNOWN) ||
                   (rc.canSenseLocation(nextSpot) &&
                    rc.senseRobotAtLocation(nextSpot) != null) ||
                   (avoidTowers && nearTower))
                {
                    goingAround = true;
                }
                else if(lastDir == dog.directionTo(target))
                {
                    goingAround = false;
                }

                //while way is blocked, rotate till free
                while((rc.canSenseLocation(nextSpot) &&
                       !rc.senseTerrainTile(nextSpot).isTraversable() &&
                       rc.senseTerrainTile(nextSpot) != TerrainTile.UNKNOWN) ||
                      (rc.canSenseLocation(nextSpot) &&
                       rc.senseRobotAtLocation(nextSpot) != null) ||
                      (avoidTowers && nearTower))
                {
                    if(goingLeft)
                    {
                        lastDir = lastDir.rotateLeft();
                    }
                    else
                    {
                        lastDir = lastDir.rotateRight();
                    }
                    nextSpot = dog.add(lastDir);
                    if(avoidTowers)
                    {
                        nearTower = false;
                        MapLocation[] towers = rc.senseEnemyTowerLocations();
                        for(int k = 0; k < towers.length; k++)
                        {
                            if(nextSpot.distanceSquaredTo(towers[k]) <= 24)
                            {
                                nearTower = true;
                            }
                        }
                        if(nextSpot.distanceSquaredTo(rc.senseEnemyHQLocation()) <= 24)
                        {
                            nearTower = true;
                        }
                    }
                }
            }
            else
            {
                if((rc.canSenseLocation(nextSpot) &&
                   (rc.senseTerrainTile(nextSpot) == TerrainTile.OFF_MAP ||
                    rc.senseTerrainTile(nextSpot) == TerrainTile.UNKNOWN)) ||
                   (rc.canSenseLocation(nextSpot) &&
                    rc.senseRobotAtLocation(nextSpot) != null) ||
                   (avoidTowers && nearTower))
                {
                    goingAround = true;
                }
                else if(lastDir == dog.directionTo(target))
                {
                    goingAround = false;
                }

                //while way is blocked, rotate till free
                while((rc.canSenseLocation(nextSpot) &&
                      (rc.senseTerrainTile(nextSpot) == TerrainTile.OFF_MAP ||
                       rc.senseTerrainTile(nextSpot) == TerrainTile.UNKNOWN)) ||
                      (rc.canSenseLocation(nextSpot) &&
                       rc.senseRobotAtLocation(nextSpot) != null) ||
                      (avoidTowers && nearTower))
                {
                    if(goingLeft)
                    {
                        lastDir = lastDir.rotateLeft();
                    }
                    else
                    {
                        lastDir = lastDir.rotateRight();
                    }
                    nextSpot = dog.add(lastDir);
                    if(avoidTowers)
                    {
                        nearTower = false;
                        MapLocation[] towers = rc.senseEnemyTowerLocations();
                        for(int k = 0; k < towers.length; k++)
                        {
                            if(nextSpot.distanceSquaredTo(towers[k]) <= 24)
                            {
                                nearTower = true;
                            }
                        }
                        if(nextSpot.distanceSquaredTo(rc.senseEnemyHQLocation()) <= 24)
                        {
                            nearTower = true;
                        }
                    }
                }

            }

            lastFacing = lastDir;
            //move dog
            dog = dog.add(lastDir);
        }

        //now go back one so in sight if not at target
        if(!dog.equals(target))
        {
            dog = dog.subtract(lastDir);
        }
    }

    //returns true if dog is in sight of human
    private boolean dogInSight(MapLocation dog, boolean avoidTowers, boolean isDrone) throws GameActionException
    {
        //start one closer to dog's location since we can get to where we are
        MapLocation currentLocation = rc.getLocation().add(rc.getLocation().directionTo(dog));
        boolean done = false;
        //go through each spot
        while(!done)
        {
            TerrainTile tile = rc.senseTerrainTile(currentLocation);
            if((!isDrone && !tile.isTraversable()) ||
               tile == TerrainTile.UNKNOWN)
            {
                return false;
            }
            else if(rc.canSenseLocation(currentLocation) &&
                    !currentLocation.equals(rc.getLocation()) &&
                    rc.senseRobotAtLocation(currentLocation) != null)
            {
                return false;
            }
            if(avoidTowers)
            {
                MapLocation[] towers = rc.senseEnemyTowerLocations();
                for(int k = 0; k < towers.length; k++)
                {
                    if(currentLocation.distanceSquaredTo(towers[k]) <= 24)
                    {
                        return false;
                    }
                }
                if(currentLocation.distanceSquaredTo(rc.senseEnemyHQLocation()) <= 24)
                {
                    return false;
                }
            }
            if(currentLocation.equals(dog))
            {
                done = true;
            }
            currentLocation = currentLocation.add(currentLocation.directionTo(dog));
        }

        //if no where is blocked, move on
        return true;
    }

    public boolean badMovement(MapLocation target) throws GameActionException
    {
        if (!rc.isCoreReady())
        {
            return false;
        }

        if (rc.getLocation().equals(target))
        {
            return false;
        }

        Direction dir = rc.getLocation().directionTo(target);
        if (rc.canMove(dir))
        {
            rc.move(dir);
            return true;
        }
        else
        {
            while (!rc.canMove(dir))
            {
                dir = dir.rotateRight();
            }
            rc.move(dir);
            return true;
        }
    }
}
