package _teammaster.Units;


import battlecode.common.*;
import _teammaster.Messaging;
import _teammaster.Unit;
import _teammaster.Utilities;

import java.util.Random;

public class SearchAndDestroyDrone extends Drone
{
    MapLocation target;
    Random rand;
    MapLocation nearestDrone;
    int roundNumb;

    public SearchAndDestroyDrone(RobotController rc)
    {
        super(rc);
        target = rc.getLocation();
        rand = new Random(rc.getID());
        rc.setIndicatorString(0, "Search and Destroy drone");
        roundNumb = Clock.getRoundNum();
    }

    public void collectData2() throws GameActionException
    {
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

        rc.setIndicatorString(1, "Target" + target);
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfDronesOdd.ordinal(), Messaging.NumbOfDronesEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (rc.getLocation().distanceSquaredTo(target) <= 35 || (roundNumb + 100) < Clock.getRoundNum())
        {
            target = findNextTarget();
            roundNumb = Clock.getRoundNum();
        }
        if (nearestDrone != null && !nearestDrone.equals(rc.getLocation()))
        {
            target = nearestDrone;
            roundNumb = Clock.getRoundNum();
        }

        rc.setIndicatorString(1, "In Navigator");
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
        //30% chance or no factories found for looking for miner
        if(toReturn.equals(rc.getLocation()))
        {
            toReturn = tracker.getRandomMiner();
        }
        if(toReturn.equals(rc.getLocation()))
        {
            MapLocation[] towers = rc.senseEnemyTowerLocations();
            // go the enemy HQ most of the time
            int decision = rand.nextInt(towers.length + 10);
            if (decision >= towers.length)
            {
                toReturn = rc.senseEnemyHQLocation();
            }
            else
            {
                toReturn = towers[decision];
            }
        }

        return toReturn;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
