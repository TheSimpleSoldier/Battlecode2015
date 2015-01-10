package team044.Units;


import battlecode.common.*;
import team044.Unit;

import java.util.Random;

public class SearchAndDestroyDrone extends Drone
{
    MapLocation target;
    Random rand;
    MapLocation nearestDrone;

    public SearchAndDestroyDrone(RobotController rc)
    {
        super(rc);
        target = rc.getLocation();
        rand = new Random();
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        int closest = -1;
        double nearestDist = Math.sqrt(Math.pow(GameConstants.MAP_MAX_HEIGHT, 2) +
                                       Math.pow(GameConstants.MAP_MAX_WIDTH, 2));
        for(int k = 0; k < nearByEnemies.length; k++)
        {
            if(nearByEnemies[k].type == RobotType.MINER ||
               nearByEnemies[k].type == RobotType.MINERFACTORY &&
               nearByEnemies[k].location.distanceSquaredTo(rc.getLocation()) < nearestDist)
            {
                nearestDist = nearByEnemies[k].location.distanceSquaredTo(rc.getLocation());
                closest = k;
            }
        }

        if(closest != -1)
        {
            nearestDrone = nearByEnemies[closest].location;
        }
        else
        {
            nearestDrone = rc.getLocation();
        }
    }

    public boolean takeNextStep() throws GameActionException
    {
        if(rc.getLocation().equals(target))
        {
            target = findNextTarget();
        }
        if(!nearestDrone.equals(rc.getLocation()))
        {
            target = nearestDrone.add(Direction.NONE);
        }

        return nav.takeNextStep(target);
    }

    private MapLocation findNextTarget() throws GameActionException
    {
        int choice = rand.nextInt(10);

        MapLocation toReturn = rc.getLocation();
        //70% chance of looking for mine factory
        if(choice < 7)
        {
            toReturn = tracker.getRandomMinerFactory();
        }
        //40% chance or no factories found for looking for miner
        if(toReturn.equals(rc.getLocation()))
        {
            toReturn = tracker.getRandomMiner();
        }
        if(toReturn.equals(rc.getLocation()))
        {
            toReturn = rc.senseEnemyHQLocation();
        }

        return toReturn;
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
