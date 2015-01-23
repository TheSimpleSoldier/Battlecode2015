package team044;

import battlecode.common.*;

import java.util.Random;

/**
 * Created by joshua on 1/22/15.
 * This is a better version of navigator that has bug fixes,
 * code optimizations, and new features over the other one.
 */
public class Navigator2
{
    private RobotController rc;
    private boolean circle, avoidTowers, avoidHQ;
    private boolean goingAround, goingLeft, initialRun, turnedAround;
    private MapLocation dog, tempDog, target, tempTarget;
    private MapLocation lastLoc;
    private Direction lastFacing;
    private int circlingTime;
    private int HQRange;

    public Navigator2(RobotController rc, boolean circle, boolean avoidTowers, boolean avoidHQ)
    {
        this.rc = rc;
        this.circle = circle;
        this.avoidTowers = avoidTowers;
        this.avoidHQ = avoidHQ;
        dog = rc.getLocation();
        tempDog = rc.getLocation();
        target = rc.getLocation();
        tempTarget = rc.getLocation();
        lastLoc = rc.getLocation();
        circlingTime = 0;
        HQRange = 52;
        goingAround = false;
        goingLeft = false;
        initialRun = true;
        turnedAround = false;
        lastFacing = Direction.NONE;
    }

    public boolean takeNextStep(MapLocation target) throws GameActionException
    {
        MapLocation myLoc = rc.getLocation();
        MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
        if (enemyTowers.length >= 5)
        {
            HQRange = 52;
        }
        else if (enemyTowers.length >= 2)
        {
            HQRange = 35;
        }
        else
        {
            HQRange = 24;
        }
        if(!target.equals(this.target))
        {
            this.target = target;
            tempTarget = null;
            dog = myLoc;
            tempDog = null;
            initialRun = true;
        }

        if(!circle && myLoc.distanceSquaredTo(target) <= 100)
        {
            if(cantGetCloser())
            {
                double radius = myLoc.distanceSquaredTo(target);
                if(circlingTime > radius)
                {
                    return false;
                }
                circlingTime++;
            }
            else
            {
                circlingTime = 0;
            }
        }
        else
        {
            circlingTime = 0;
        }

        if(rc.getType() == RobotType.COMMANDER)
        {
            dogGo(enemyTowers);
        }
        else
        {
            dogGo(enemyTowers);
        }

        if(rc.isCoreReady())
        {
            if(rc.canMove(myLoc.directionTo(dog)) && !badSpot(myLoc.add(myLoc.directionTo(dog)), enemyTowers))
            {
                rc.move(myLoc.directionTo(dog));
                return true;
            }
            else if(rc.getType() == RobotType.COMMANDER)
            {
               if(myLoc.distanceSquaredTo(dog) <= 10)
               {
                   if(rc.getFlashCooldown() == 0 && rc.isCoreReady())
                   {
                       rc.castFlash(dog);
                       return true;
                   }
                   else
                   {
                       return false;
                   }
               }
            }
            else
            {
                dog = myLoc;
                tempDog = null;
                initialRun = true;
            }
        }

        return false;
    }

    private void commanderDogGo(MapLocation[] towers)
    {

    }

    private void dogGo(MapLocation[] towers) throws GameActionException
    {
        int round = Clock.getRoundNum();
        MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
        if(initialRun)
        {
            while(!badSpot(dog, enemyTowers))
            {
                dog = dog.add(dog.directionTo(target));
                if(Clock.getBytecodesLeft() < 500 || Clock.getRoundNum() != round)
                {
                    return;
                }
            }
            dog = dog.subtract(dog.directionTo(target));
        }

        Direction lastDir = Direction.NONE;

        while(dogInSight(enemyTowers) && !dog.equals(target))
        {
            //prevents bugging around exterior of map
            if(goingAround && buggingAroundBorder())
            {
                goingLeft = !goingLeft;
                if(turnedAround)
                {
                    dog = rc.getLocation();
                    turnedAround = false;
                    goingAround = false;
                }
                else
                {
                    turnedAround = true;
                }
                lastFacing = lastFacing.opposite();
            }

            MapLocation nextSpot = dog.add(lastFacing);
            //preventing it getting stuck in bowls
            if(goingAround)
            {
                lastDir = lastFacing;
                nextSpot = dog.add(lastDir);
                while(!badSpot(nextSpot, enemyTowers))
                {
                    if(goingLeft)
                    {
                        lastDir = lastDir.rotateRight();
                    }
                    else
                    {
                        lastDir = lastDir.rotateLeft();
                    }
                    nextSpot = dog.add(lastDir);
                }
            }
            else
            {
                lastDir = dog.directionTo(target);
            }

            if(badSpot(nextSpot, towers))
            {
                if(!goingAround)
                {
                    goingAround = true;
                    goingLeft = goLeft(lastDir);
                }
            }
            else if(lastDir == dog.directionTo(target))
            {
                goingAround = false;
                turnedAround = false;
            }

            //while way is blocked, rotate till free
            while(badSpot(nextSpot, towers))
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
            }

            lastFacing = lastDir;
            dog = dog.add(lastDir);
            if(Clock.getBytecodesLeft() < 1500 || Clock.getRoundNum() != round)
            {
                return;
            }
        }

        if(!dog.equals(target))
        {
            dog.subtract(lastDir);
        }
    }

    //returns true if dog is in sight of human
    private boolean dogInSight(MapLocation[] towers) throws GameActionException
    {
        //start one closer to dog's location since we can get to where we are
        MapLocation currentLocation = rc.getLocation().add(rc.getLocation().directionTo(dog));

        //loop through until it either reaches the goal or finds a bad spot
        while(true)
        {
            if(badSpot(currentLocation, towers))
            {
                return false;
            }
            if(currentLocation.equals(dog))
            {
                return true;
            }
            currentLocation = currentLocation.add(currentLocation.directionTo(dog));
        }
    }

    private boolean badSpot(MapLocation spot, MapLocation[] towers) throws GameActionException
    {
        if(checkEnemyMainStructures(spot, towers))
        {
            return true;
        }

        if(rc.canSenseLocation(spot))
        {
            TerrainTile tile = rc.senseTerrainTile(spot);
            if(tile == TerrainTile.UNKNOWN || tile == TerrainTile.OFF_MAP)
            {
                return true;
            }
            if(rc.getType() != RobotType.DRONE && tile == TerrainTile.VOID)
            {
                return true;
            }

            RobotInfo bot = null;
            if (rc.canSenseLocation(spot))
            {
                bot = rc.senseRobotAtLocation(spot);
            }

            if(bot != null && rc.getID() != bot.ID && rc.getLocation().distanceSquaredTo(bot.location) < 15)
            {
                return true;
            }
        }

        return true;
    }

    //this takes into account flags to check if we are near enemy towers or hq
    private boolean checkEnemyMainStructures(MapLocation spot, MapLocation[] towers)
    {
        if(avoidTowers)
        {
            for(int k = 0; k < towers.length; k++)
            {
                if(spot.distanceSquaredTo(towers[k]) <= 24)
                {
                    return true;
                }
            }
        }
        if(avoidHQ)
        {
            if(spot.distanceSquaredTo(rc.senseEnemyHQLocation()) <= HQRange)
            {
                return true;
            }
        }

        return false;
    }

    //this checks if the target cannot be reached by the robot
    private boolean cantGetCloser() throws GameActionException
    {
        MapLocation[] towers = rc.senseEnemyTowerLocations();
        if(!badSpot(target, towers))
        {
            RobotInfo bot = null;
            if (rc.canSenseLocation(target))
            {
                bot = rc.senseRobotAtLocation(target);
            }

            if(bot == null)
            {
                return false;
            }
        }
        MapLocation currentLocation = rc.getLocation();
        while(!currentLocation.equals(target))
        {
            if(!badSpot(currentLocation, towers))
            {
                RobotInfo bot = null;
                if (rc.canSenseLocation(currentLocation))
                {
                    bot = rc.senseRobotAtLocation(currentLocation);
                }

                if(bot == null)
                {
                    return false;
                }
            }
            currentLocation = currentLocation.add(currentLocation.directionTo(target));
        }

        return true;
    }

    //this is a simple check to make sure we do not try to bug around the entire map
    private boolean buggingAroundBorder()
    {

        Direction towardTarget = dog.directionTo(target);
        if(goingLeft)
        {
            if(rc.senseTerrainTile(dog.add(lastFacing.rotateRight().rotateRight())) == TerrainTile.OFF_MAP &&
               lastFacing != towardTarget && lastFacing.rotateRight() != towardTarget &&
               lastFacing.rotateLeft() != towardTarget &&
               lastFacing.rotateRight().rotateRight() != towardTarget &&
               lastFacing.rotateLeft().rotateLeft() != towardTarget)
            {
                return true;
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
                return true;
            }
        }

        return false;
    }

    //returns true if we should bug left around the roadblock, false means go right
    private boolean goLeft(Direction lastDir) throws GameActionException
    {
        MapLocation[] towers = rc.senseEnemyTowerLocations();
        if(!badSpot(dog.add(lastDir.rotateLeft()), towers))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight()), towers))
        {
            return false;
        }
        else if(!badSpot(dog.add(lastDir.rotateLeft().rotateLeft()), towers))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight().rotateRight()), towers))
        {
            return false;
        }
        else if(!badSpot(dog.add(lastDir.rotateLeft().rotateLeft().rotateLeft()), towers))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight().rotateRight().rotateRight()), towers))
        {
            return false;
        }

        //the only spot is behind us, so random's guess is as good as mine
        return new Random().nextBoolean();
    }

    public void setCircle(boolean circle)
    {
        this.circle = circle;
    }

    public void setAvoidTowers(boolean avoidTowers)
    {
        this.avoidTowers = avoidTowers;
    }

    public void setAvoidHQ(boolean avoidHQ)
    {
        this.avoidHQ = avoidHQ;
    }
}
