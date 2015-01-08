package team044.Units;


import battlecode.common.*;
import team044.Unit;

import java.util.Random;

public class ScoutingDrone extends Drone
{
    MapLocation target;
    Random rand;
    public ScoutingDrone(RobotController rc)
    {
        super(rc);
        rc.setIndicatorString(0, "Scouting Drone");
        target = rc.getLocation();
        rand = new Random();
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        if(Clock.getRoundNum() % 20 == 0)
        {
            //Utilities.getBestSpot(rc, false);
        }
    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
    }

    public boolean takeNextStep() throws GameActionException
    {
        if(rc.getLocation().equals(target))
        {
            target = findNextTarget();
        }
        if(rc.senseTerrainTile(target) == TerrainTile.OFF_MAP)
        {
            target = findNextTarget();
        }

        rc.setIndicatorString(1, "target: " + target.toString());

        return nav.badMovement(target);
    }

    private MapLocation findNextTarget()
    {
        MapLocation[] tendrils = new MapLocation[8];
        for(int k = 0; k < tendrils.length; k++)
        {
            tendrils[k] = rc.getLocation();
        }

        Direction[] dirs = Direction.values();

        boolean done = false;
        while(!done)
        {
            done = true;

            for(int k = 0; k < 8; k++)
            {
                tendrils[k] = tendrils[k].add(dirs[k]);
                if(rc.senseTerrainTile(tendrils[k]) == TerrainTile.UNKNOWN)
                {
                    return tendrils[k];
                }
                if(rc.senseTerrainTile(tendrils[k]) != TerrainTile.OFF_MAP)
                {
                    done = false;
                }
                else
                {
                    tendrils[k] = tendrils[k].subtract(dirs[k]);
                }
            }
        }

        MapLocation next = rc.getLocation().add(dirs[rand.nextInt(8)]);
        while(!rc.canMove(rc.getLocation().directionTo(next)))
        {
            next = rc.getLocation().add(dirs[rand.nextInt(8)]);
        }

        return next;
    }

    public boolean fight() throws GameActionException
    {
        return false;
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
