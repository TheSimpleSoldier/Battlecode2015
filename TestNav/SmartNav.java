package TestNav;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.TerrainTile;

/**
 * Created by joshua on 1/14/15.
 * This class will handle all creation of void analysis
 */
public class SmartNav
{
    RobotController rc;

    public SmartNav(RobotController rc)
    {
        this.rc = rc;
    }

    //this takes a spot on a void and analyzes the entire void
    public void analyzeVoid(int x, int y)
    {
        //start by bugging the inside to find dimmensions to use
        int minX = x;
        int minY = y;
        int maxX = x;
        int maxY = y;

        MapLocation currentLoc = new MapLocation(x, y);

        while(rc.senseTerrainTile(currentLoc.add(Direction.NORTH)) == TerrainTile.VOID)
        {
            currentLoc = currentLoc.add(Direction.NORTH);
            if(currentLoc.x < minX)
            {
                minX = currentLoc.x;
            }
            if(currentLoc.y < minY)
            {
                minY = currentLoc.y;
            }
            if(currentLoc.x > maxX)
            {
                maxX = currentLoc.x;
            }
            if(currentLoc.y > maxY)
            {
                maxY = currentLoc.y;
            }
        }

        MapLocation startBugLocation = currentLoc.add(Direction.NONE);
        Direction lastDir = Direction.NORTH_WEST;

        do
        {
            lastDir = lastDir.rotateRight();
            while(rc.senseTerrainTile(currentLoc.add(lastDir)) != TerrainTile.VOID)
            {
                lastDir = lastDir.rotateLeft();
            }
            currentLoc = currentLoc.add(lastDir);
            if(currentLoc.x < minX)
            {
                minX = currentLoc.x;
            }
            if(currentLoc.y < minY)
            {
                minY = currentLoc.y;
            }
            if(currentLoc.x > maxX)
            {
                maxX = currentLoc.x;
            }
            if(currentLoc.y > maxY)
            {
                maxY = currentLoc.y;
            }
        }
        while(!currentLoc.equals(startBugLocation));


    }

}
