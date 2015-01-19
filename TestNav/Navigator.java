package TestNav;

import battlecode.common.*;

import java.util.Random;

public class Navigator
{
    private RobotController rc;
    private MapLocation dog, target;
    private Random rand;
    private boolean goingLeft, goingAround;
    private boolean avoidTowers, avoidHQ, ignoreVoids, lowBytecodes, virtualVoids;
    private Direction lastFacing;
    private int HQRange = 24;
    private Void[] voids;
    private Void dogCurrentVoid, rcCurrentVoid;
    private int lastVoid;

    public Navigator(RobotController rc, boolean avoidTowers, boolean avoidHQ,
                     boolean lowBytecodes, boolean virtualVoids)
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
        this.virtualVoids = virtualVoids;
        voids = new Void[200];
        dogCurrentVoid = null;
        rcCurrentVoid = null;
        lastVoid = 0;

        if(rc.getType() == RobotType.DRONE || rc.getType() == RobotType.MISSILE)
        {
            ignoreVoids = true;
        }
        else
        {
            ignoreVoids = false;
        }

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
    }

    //This method uses a walk the dog method
    //The walk the dog method has a dog that runs away till the owner
    //is about to lose sight of it, then it sits there until the owner catches up.
    //The dog moves in a bug pattern, but the owner will cut corners.
    public boolean takeNextStep(MapLocation target) throws GameActionException
    {
        MapLocation myLocation = rc.getLocation();
        if(virtualVoids)
        {
            rcCurrentVoid = updateVoid(myLocation, rcCurrentVoid);
        }
        //if target changed, act like dog is next to owner
        if(!target.equals(this.target))
        {
            dog = myLocation;
            this.target = target;
        }

        //dog always tries to run ahead since it will sometimes be stopped early
        dogGo();

        Direction dir = myLocation.directionTo(dog);
        MapLocation[] towers = rc.senseEnemyTowerLocations();

        //if you can move towards the dog, do
        if (!badSpot(myLocation.add(dir), towers, rcCurrentVoid, true) && rc.canMove(dir) && rc.isCoreReady())
        {
            rc.move(dir);
            return true;
        }
        //if it is another unit, go around it
        else if(rc.canSenseLocation(myLocation.add(dir)) &&
                rc.senseRobotAtLocation(myLocation.add(dir)) != null  && rc.isCoreReady())
        {

            if(!badSpot(myLocation.add(dir.rotateRight()), towers, rcCurrentVoid, true) && rc.canMove(dir.rotateRight()))
            {
                rc.move(dir.rotateRight());
            }
            else if(!badSpot(myLocation.add(dir.rotateLeft()), towers, rcCurrentVoid, true) && rc.canMove(dir.rotateLeft()))
            {
                rc.move(dir.rotateLeft());
            }
            else if(!badSpot(myLocation.add(dir.rotateRight().rotateRight()), towers, rcCurrentVoid, true) && rc.canMove(dir.rotateRight().rotateRight()))
            {
                rc.move(dir.rotateRight().rotateRight());
            }
            else if(!badSpot(myLocation.add(dir.rotateLeft().rotateLeft()), towers, rcCurrentVoid, true) && rc.canMove(dir.rotateLeft().rotateLeft()))
            {
                rc.move(dir.rotateLeft().rotateLeft());
            }
        }
        //otherwise, if you can move, something is in the way, so reroute
        else if(rc.isCoreReady())
        {
            dog = myLocation;
        }

        return false;
    }

    //This is the method that moves the dog along till it is almost out of sight
    private void dogGo() throws GameActionException
    {
        int round = Clock.getRoundNum();
        Direction lastDir = Direction.NONE;

        MapLocation[] towers = rc.senseEnemyTowerLocations();
        //go till out of site
        while(dogInSight(towers) && !dog.equals(target))
        {
            boolean vVoids = true;
            if(dogCurrentVoid != null && dogCurrentVoid.getSpotValue(dog) > 0 &&
               dogCurrentVoid.getSpotValue(dog) == dogCurrentVoid.getSpotValue(target))
            {
                vVoids = false;
            }
            if(virtualVoids)
            {
                dogCurrentVoid = updateVoid(dog, dogCurrentVoid);
            }
            if(lowBytecodes && (Clock.getBytecodesLeft() < 1500 || Clock.getRoundNum() != round))
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

            if(badSpot(nextSpot, towers, dogCurrentVoid, vVoids))
            {
                if(!goingAround)
                {
                    goingAround = true;
                    goingLeft = goLeft(lastDir, vVoids);
                }
            }
            else if(lastDir == dog.directionTo(target))
            {
                goingAround = false;
            }

            //while way is blocked, rotate till free
            while(badSpot(nextSpot, towers, dogCurrentVoid, vVoids))
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
    private boolean dogInSight(MapLocation[] towers) throws GameActionException
    {
        //start one closer to dog's location since we can get to where we are
        MapLocation currentLocation = rc.getLocation().add(rc.getLocation().directionTo(dog));

        //loop through until it either reaches the goal or finds a bad spot
        while(true)
        {
            if(virtualVoids)
            {
                if(badSpot(currentLocation, towers, updateVoid(currentLocation, null), true))
                {
                    return false;
                }
            }
            else
            {
                if(badSpot(currentLocation, towers, null, false))
                {
                    return false;
                }
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

    //this returns true if the spot is bad for any reason
    //such as void if not drone, unknown, off map, enemy towers/hq, or our own structures
    private boolean badSpot(MapLocation spot, MapLocation[] towers, Void currentVoid, boolean vVoids) throws GameActionException
    {
        if(checkEnemyMainStructures(spot, towers))
        {
            return true;
        }

        if(virtualVoids && vVoids)
        {
            if(currentVoid != null)
            {
                int spotValue = currentVoid.getSpotValue(spot);
                if(spotValue == -2 || spotValue == -1 || spotValue > 0)
                {
                    return true;
                }
            }
        }

        TerrainTile tile = rc.senseTerrainTile(spot);
        if(tile == TerrainTile.UNKNOWN || tile == TerrainTile.OFF_MAP)
        {
            return true;
        }
        if(!ignoreVoids && tile == TerrainTile.VOID)
        {
            return true;
        }

        return false;
    }

    //returns true if we should bug left around the roadblock, false means go right
    private boolean goLeft(Direction lastDir, boolean vVoids) throws GameActionException
    {
        MapLocation[] towers = rc.senseEnemyTowerLocations();
        if(!badSpot(dog.add(lastDir.rotateLeft()), towers, dogCurrentVoid, vVoids))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight()), towers, dogCurrentVoid, vVoids))
        {
            return false;
        }
        else if(!badSpot(dog.add(lastDir.rotateLeft().rotateLeft()), towers, dogCurrentVoid, vVoids))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight().rotateRight()), towers, dogCurrentVoid, vVoids))
        {
            return false;
        }
        else if(!badSpot(dog.add(lastDir.rotateLeft().rotateLeft().rotateLeft()), towers, dogCurrentVoid, vVoids))
        {
            return true;
        }
        else if(!badSpot(dog.add(lastDir.rotateRight().rotateRight().rotateRight()), towers, dogCurrentVoid, vVoids))
        {
            return false;
        }

        //the only spot is behind us, so random's guess is as good as mine
        return rand.nextBoolean();
    }

    private Void updateVoid(MapLocation location, Void currentVoid) throws GameActionException
    {
        if(currentVoid != null && nearVoid(location, currentVoid.startX, currentVoid.startY,
                                           currentVoid.width, currentVoid.height))
        {
            return currentVoid;
        }
        else if(lastVoid > 0)
        {
            for(int k = 0; k < lastVoid; k++)
            {
                if(nearVoid(location, voids[k].startX, voids[k].startY,voids[k].width, voids[k].height))
                {
                    return voids[k];
                }
            }
        }

        int lastHeader = rc.readBroadcast(Constants.startNavChannels);
        for(int k = Constants.startNavChannels + 2; k < lastHeader; k += 5)
        {
            int startX = rc.readBroadcast(k + 1);
            int startY = rc.readBroadcast(k + 2);
            int width = rc.readBroadcast(k + 3);
            int height = rc.readBroadcast(k + 4);
            if(nearVoid(location, startX, startY, width, height))
            {
                int areaStart = rc.readBroadcast(k);
                Void temp = new Void(downloadArea(areaStart, width, height), startX, startY, width, height);
                voids[lastVoid] = temp;
                lastVoid++;
                return temp;
            }
        }

        return null;
    }

    private int[][] downloadArea(int start, int width, int height) throws GameActionException
    {
        int[][] area = new int[height][width];

        int counter = 0;
        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                area[k][a] = rc.readBroadcast(start + counter);
                counter++;
            }
        }

        return area;
    }

    private boolean nearVoid(MapLocation location, int startX, int startY, int width, int height)
    {
        if(location.x + 1 >= startX && location.x - 1 <= startX + width &&
           location.y + 1 >= startY && location.y - 1 <= startY + height)
        {
            return true;
        }

        return false;
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

    public void setVirtualVoids(boolean virtualVoids)
    {
        this.virtualVoids = virtualVoids;
    }

    public MapLocation getTarget()
    {
        return target;
    }

}
