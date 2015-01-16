package TestNav;

import battlecode.common.*;

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
    public boolean analyzeVoid(int x, int y) throws GameActionException
    {
        int[] dims = findBorders(x, y);

        if(dims == null)
        {
            return false;
        }

        int[][] area = createVoidMap(dims);

        if(area == null)
        {
            return false;
        }

        /*
        The following is purely for testing purposes
        if(rc.readBroadcast(12865) != 1)
        {
            rc.broadcast(12865, 1);
            System.out.println("start");
            for(int k = 0; k < dims.length; k++)
            {
                System.out.println(dims[k]);
            }
            for(int k = 0; k < area.length; k++)
            {
                String line = "";
                for(int a = 0; a < area[0].length; a++)
                {
                    line += area[k][a] + " ";
                }
                System.out.println(line);
            }
            rc.broadcast(12865, 0);
        }*/

        return true;
    }

    /*
    The goal of this method is to find, from a single location resting on a void,
    the smallest rectangle that contains the void feature. If unknowns are seen or
    if the original location is not on a void, the function returns null.

    The way this is accomplished is that a dog goes north till it reaches the edge
    and then it bugs around the inside of the void till it reaches the start again,
    all the while updating the min and max values. Once this is done, the min and
    max values of the voids, which is also the smallest rectangle, is known and can
    be returned.
     */
    private int[] findBorders(int x, int y)
    {
        int[] dims = {x,y,x,y};

        MapLocation currentLoc = new MapLocation(x, y);

        if(rc.senseTerrainTile(currentLoc) != TerrainTile.VOID)
        {
            return null;
        }

        while(rc.senseTerrainTile(currentLoc.add(Direction.NORTH)) == TerrainTile.VOID)
        {
            currentLoc = currentLoc.add(Direction.NORTH);
            if(currentLoc.x < dims[0])
            {
                dims[0] = currentLoc.x;
            }
            if(currentLoc.y < dims[1])
            {
                dims[1] = currentLoc.y;
            }
            if(currentLoc.x > dims[2])
            {
                dims[2] = currentLoc.x;
            }
            if(currentLoc.y > dims[3])
            {
                dims[3] = currentLoc.y;
            }
        }

        MapLocation startBugLocation = currentLoc.add(Direction.NONE);
        Direction firstDir = Direction.NORTH;
        while(rc.senseTerrainTile(currentLoc.add(firstDir)) != TerrainTile.VOID)
        {
            firstDir = firstDir.rotateRight();
        }
        firstDir = firstDir.opposite();

        Direction lastDir = Direction.WEST;


        do
        {
            lastDir = lastDir.rotateRight().rotateRight();
            while(rc.senseTerrainTile(currentLoc.add(lastDir)) != TerrainTile.VOID)
            {
                if(rc.senseTerrainTile(currentLoc.add(lastDir)) == TerrainTile.UNKNOWN)
                {
                    return null;
                }
                lastDir = lastDir.rotateLeft().rotateLeft();
            }
            currentLoc = currentLoc.add(lastDir);
            if(currentLoc.x < dims[0])
            {
                dims[0] = currentLoc.x;
            }
            if(currentLoc.y < dims[1])
            {
                dims[1] = currentLoc.y;
            }
            if(currentLoc.x > dims[2])
            {
                dims[2] = currentLoc.x;
            }
            if(currentLoc.y > dims[3])
            {
                dims[3] = currentLoc.y;
            }
        }
        while(!(currentLoc.equals(startBugLocation) && firstDir == lastDir));

        return dims;
    }

    /*
    The goal of this function is to read in the entire affected region of the void
    and to return a 2d int array that has values 0 for normal spaces, 1 for voids,
    and 2 for off map. If any of the spots are unknown, the function will return
    null since unknown spots mean we will not be able to do any more analysis.
     */
    private int[][] createVoidMap(int[] dims)
    {
        int height = dims[3] - dims[1] + 1;
        int width = dims[2] - dims[0] + 1;

        int[] newDims = new int[4];

        //update max and min so entire area being read is known
        newDims[0] = (int)(dims[0] - (height / 2. - .1));
        newDims[1] = (int)(dims[1] - (width / 2. - .1));
        newDims[2] = (int)(dims[2] + (height / 2. + .9));
        newDims[3] = (int)(dims[3] + (width / 2. + .9));

        for(int k = 0; k < newDims.length; k++)
        {
            if(newDims[k] < 0)
            {
                newDims[k]--;
            }
        }

        //These for loops are checks for the edge of the map, because beyond the edge
        //of map spots are continued by unknowns deeper, this ensures we do not have
        //a false negative
        for(int k = dims[0]; k > newDims[0]; k--)
        {
            if(rc.senseTerrainTile(new MapLocation(k, dims[1])) == TerrainTile.OFF_MAP)
            {
                newDims[0] = k;
                break;
            }
        }
        for(int k = dims[1]; k > newDims[1]; k--)
        {
            if(rc.senseTerrainTile(new MapLocation(dims[0], k)) == TerrainTile.OFF_MAP)
            {
                newDims[1] = k;
                break;
            }
        }
        for(int k = dims[2]; k < newDims[2]; k++)
        {
            if(rc.senseTerrainTile(new MapLocation(k, dims[3])) == TerrainTile.OFF_MAP)
            {
                newDims[2] = k;
                break;
            }
        }
        for(int k = dims[3]; k < newDims[3]; k++)
        {
            if(rc.senseTerrainTile(new MapLocation(dims[2], k)) == TerrainTile.OFF_MAP)
            {
                newDims[3] = k;
                break;
            }
        }

        height = newDims[3] - newDims[1] + 1;
        width = newDims[2] - newDims[0] + 1;

        int[][] area = new int[height][width];

        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                int currentX = a + newDims[0];
                int currentY = k + newDims[1];
                TerrainTile tile = rc.senseTerrainTile(new MapLocation(currentX, currentY));

                if(tile == TerrainTile.NORMAL)
                {
                    area[k][a] = 0;
                }
                else if(tile == TerrainTile.VOID)
                {
                    area[k][a] = 1;
                }
                else if(tile == TerrainTile.OFF_MAP)
                {
                    area[k][a] = 2;
                }
                else
                {
                    //spot is unknown, so the function cannot finish
                    return null;
                }
            }
        }

        return area;
    }

}
