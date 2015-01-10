package team044;

import battlecode.common.*;

import java.util.Random;

public class Navigator
{
    private RobotController rc;
    private MapLocation dog, target;
    private Random rand;
    private boolean goingLeft, goingAround;
    private boolean avoidTowers, avoidHQ, ignoreVoids, lowBytecodes, badDog;
    private Direction lastFacing;

    public Navigator(RobotController rc, boolean avoidTowers, boolean avoidHQ,
                     boolean lowBytecodes, boolean badDog)
    {
        this.rc = rc;
        dog = rc.getLocation();
        target = rc.getLocation();
        rand = new Random(rc.getID());
        goingLeft = rand.nextBoolean();
        goingAround = false;
        lastFacing = Direction.NONE;
        this.avoidTowers = avoidTowers;
        this.avoidHQ = avoidHQ;
        this.lowBytecodes = lowBytecodes;
        this.badDog = badDog;

        if(rc.getType() == RobotType.DRONE || rc.getType() == RobotType.MISSILE)
        {
            ignoreVoids = true;
        }
        else
        {
            ignoreVoids = false;
        }
    }

    //This method uses a walk the dog method
    //The walk the dog method has a dog that runs away till the owner
    //is about to lose sight of it, then it sits there until the owner catches up.
    //The dog moves in a bug pattern, but the owner will cut corners.
    public boolean takeNextStep(MapLocation target) throws GameActionException
    {
        //if target changed, act like dog is next to owner
        if(!target.equals(this.target))
        {
            dog = rc.getLocation();
            this.target = target;
        }

        //dog always tries to run ahead since it will sometimes be stopped early
        dogGo();

        Direction dir = rc.getLocation().directionTo(dog);

        //if you can move towards the dog, do
        if (rc.canMove(dir) && rc.isCoreReady())
        {
            rc.move(dir);
            return true;
        }
        //if it is another unit, go around it
        else if(isUnit(rc.getLocation().add(dir)) && rc.isCoreReady())
        {
            if(!badSpot(rc.getLocation().add(dir.rotateRight())))
            {
                rc.move(dir.rotateRight());
            }
            else if(!badSpot(rc.getLocation().add(dir.rotateLeft())))
            {
                rc.move(dir.rotateLeft());
            }
            else if(!badSpot(rc.getLocation().add(dir.rotateRight().rotateRight())))
            {
                rc.move(dir.rotateRight().rotateRight());
            }
            else if(!badSpot(rc.getLocation().add(dir.rotateLeft().rotateLeft())))
            {
                rc.move(dir.rotateLeft().rotateLeft());
            }
        }
        //otherwise, if you can move, something is in the way, so reroute
        else if(rc.isCoreReady())
        {
            if(cantGetCloser())
            {
                this.target = rc.getLocation();
            }
            dog = rc.getLocation();
        }

        return false;
    }

    //This is the method that moves the dog along till it is almost out of sight
    private void dogGo() throws GameActionException
    {
        Direction lastDir = Direction.NONE;
        //go till out of site
        while(dogInSight() && !dog.equals(target))
        {
            if(lowBytecodes && Clock.getBytecodesLeft() < 1500)
            {
                return;
            }
            //This is used so the dog knows if it is going around an object
            //prevents bugging around exterior of map
            if(goingAround && buggingAroundBorder())
            {
                goingLeft = !goingLeft;
                lastFacing = lastFacing.opposite();
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

            if(badSpot(nextSpot))
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
            }

            //while way is blocked, rotate till free
            while(badSpot(nextSpot))
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
        }

        //now go back one so in sight if not at target
        if(!dog.equals(target))
        {
            dog = dog.subtract(lastDir);
        }
    }

    //returns true if dog is in sight of human
    private boolean dogInSight() throws GameActionException
    {
        //start one closer to dog's location since we can get to where we are
        MapLocation currentLocation = rc.getLocation().add(rc.getLocation().directionTo(dog));
        //loop through until it either reaches the goal or finds a bad spot
        while(true)
        {
            if(badSpot(currentLocation))
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

    //this takes into account flags to check if we are near enemy towers or hq
    private boolean checkEnemyMainStructures(MapLocation spot)
    {
        boolean nearEnemy = false;
        if(avoidTowers)
        {
            MapLocation[] towers = rc.senseEnemyTowerLocations();
            for(int k = 0; k < towers.length; k++)
            {
                if(spot.distanceSquaredTo(towers[k]) <= 25)
                {
                    nearEnemy = true;
                }
            }
        }
        if(avoidHQ)
        {
            if(spot.distanceSquaredTo(rc.senseEnemyHQLocation()) <= 35)
            {
                nearEnemy = true;
            }
        }

        return nearEnemy;
    }

    //this returns true if the spot is bad for any reason
    //such as void if not drone, unknown, off map, enemy towers/hq, or our own structures
    private boolean badSpot(MapLocation spot) throws GameActionException
    {
        boolean bad = false;

        if(checkEnemyMainStructures(spot))
        {
            bad = true;
        }

        if(rc.canSenseLocation(spot))
        {
            TerrainTile tile = rc.senseTerrainTile(spot);
            if(tile == TerrainTile.UNKNOWN || tile == TerrainTile.OFF_MAP)
            {
                bad = true;
            }
            if(!ignoreVoids && tile == TerrainTile.VOID)
            {
                bad = true;
            }

            RobotInfo bot = null;
            if (rc.canSenseLocation(spot))
            {
                bot = rc.senseRobotAtLocation(spot);
            }

            if(bot != null && rc.getID() != bot.ID && !isUnit(spot))
            {
                bad = true;
            }
        }

        return bad;
    }

    //returns true if bot is a mobile unit
    private boolean isUnit(MapLocation location) throws GameActionException
    {
        RobotInfo bot = null;
        if (rc.canSenseLocation(location))
        {
            bot = rc.senseRobotAtLocation(location);
        }


        if(bot != null)
        {
            if(bot.type == RobotType.BASHER || bot.type == RobotType.BEAVER || bot.type == RobotType.COMMANDER ||
               bot.type == RobotType.COMPUTER || bot.type == RobotType.DRONE || bot.type == RobotType.LAUNCHER ||
               bot.type == RobotType.MISSILE || bot.type == RobotType.SOLDIER || bot.type == RobotType.MINER ||
               bot.type == RobotType.TANK)
            {
                return true;
            }
        }

        return false;
    }

    //returns true if we should bug left around the roadblock, false means go right
    private boolean goLeft(Direction lastDir) throws GameActionException
    {
        if(!badSpot(dog.add(lastDir.rotateLeft())))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight())))
        {
            return false;
        }
        else if(!badSpot(dog.add(lastDir.rotateLeft().rotateLeft())))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight().rotateRight())))
        {
            return false;
        }
        else if(!badSpot(dog.add(lastDir.rotateLeft().rotateLeft().rotateLeft())))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight().rotateRight().rotateRight())))
        {
            return false;
        }

        //the only spot is behind us, so random's guess is as good as mine
        return rand.nextBoolean();
    }

    //this checks if the target cannot be reached by the robot
    private boolean cantGetCloser() throws GameActionException
    {
        if(!badSpot(target))
        {
            return false;
        }
        MapLocation currentLocation = rc.getLocation();
        while(!currentLocation.equals(target))
        {
            if(!badSpot(currentLocation))
            {
                return false;
            }
            currentLocation = currentLocation.add(currentLocation.directionTo(target));
        }

        return true;
    }

    //setters to change flags mid game
    public void setAvoidTowers(boolean avoidTowers)
    {
        this.avoidTowers = avoidTowers;
    }

    public void setAvoidHQ(boolean avoidHQ)
    {
        this.avoidHQ = avoidHQ;
    }

    public void setLowBytecodes(boolean lowBytecodes)
    {
        this.lowBytecodes = lowBytecodes;
    }

    //mainly for missiles. very bycode efficient, but also not very good
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
