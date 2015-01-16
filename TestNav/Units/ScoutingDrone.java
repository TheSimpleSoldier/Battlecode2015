package TestNav.Units;


import TestNav.SmartNav;
import battlecode.common.*;
import TestNav.Unit;
import TestNav.Utilities;

import java.util.Random;

public class ScoutingDrone extends Drone
{
    MapLocation target;
    Random rand;
    SmartNav smartNav;
    public ScoutingDrone(RobotController rc)
    {
        super(rc);
        rc.setIndicatorString(0, "Scouting Drone");
        target = rc.getLocation();
        rand = new Random();
        smartNav = new SmartNav(rc);
        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();
        if(Clock.getRoundNum() % 20 == 0)
        {
            Utilities.getBestSpot(rc, false);
        }

        if(rc.senseTerrainTile(rc.getLocation()) == TerrainTile.VOID && Clock.getRoundNum() > 1000 || true)
        {
            if(smartNav.analyzeVoid(rc.getLocation().x, rc.getLocation().y))
            {
                /*while(true)
                {
                    rc.yield();
                }*/
            }
        }
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

        return nav.takeNextStep(target);
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

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
