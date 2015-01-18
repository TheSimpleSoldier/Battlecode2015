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
        //int round = Clock.getRoundNum();
        int[] dims = findBorders(x, y);

        if(dims == null)
        {
            return false;
        }

        int[][] area = analyzeVirtualVoids(dims);

        if(area == null)
        {
            return false;
        }

        int[] newDims = findAffectedRegion(dims);

        int[][] newArea = createVoidMap(dims, newDims, area);

        if(newArea == null)
        {
            return false;
        }

        //System.out.println("rounds: " + (Clock.getRoundNum() - round));

        /*
        The following is purely for testing purposes
        */
        if(rc.readBroadcast(12865) != 1)
        {
            rc.broadcast(12865, 1);
            //System.out.println("start");
            /*for(int k = 0; k < dims.length; k++)
            {
                System.out.println(dims[k]);
            }*/
            /*for(int k = 0; k < area.length; k++)
            {
                String line = "";
                for(int a = 0; a < area[0].length; a++)
                {
                    line += area[k][a] + " ";
                }
                System.out.println(line);
            }*/

            /*for(int k = 0; k < newArea.length; k++)
            {
                String line = "";
                for(int a = 0; a < newArea[0].length; a++)
                {
                    if(newArea[k][a] == -1)
                    {
                        line += "VVV";
                    }
                    else if(newArea[k][a] == -2)
                    {
                        line += "OOO";
                    }
                    else if(newArea[k][a] == 0)
                    {
                        line += "  ";
                    }
                    else if(newArea[k][a] < 10)
                    {
                        line += " " + newArea[k][a] + " ";
                    }
                    else
                    {
                        line += newArea[k][a] + " ";
                    }
                }
                System.out.println(line);
            }*/
            /*for(int k = 0; k < dirMap.length; k++)
            {
                for(int a = 0; a < dirMap[0].length; a++)
                {
                    System.out.println("(" + a + "," + k + "): " + Integer.toBinaryString(dirMap[k][a]));
                }
            }*/

            rc.broadcast(12865, 0);
        }


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
        int count = 0;
        while(rc.senseTerrainTile(currentLoc.add(firstDir)) != TerrainTile.VOID)
        {
            firstDir = firstDir.rotateRight().rotateRight();
            if(count > 5)
            {
                return dims;
            }
            count++;
        }
        firstDir = firstDir.opposite();

        Direction lastDir = Direction.WEST;


        do
        {
            lastDir = lastDir.rotateRight().rotateRight();
            count = 0;
            while(rc.senseTerrainTile(currentLoc.add(lastDir)) != TerrainTile.VOID)
            {
                if(rc.senseTerrainTile(currentLoc.add(lastDir)) == TerrainTile.UNKNOWN)
                {
                    return null;
                }
                lastDir = lastDir.rotateLeft().rotateLeft();
                if(count > 5)
                {
                    return dims;
                }
                count++;
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

    private int[] findAffectedRegion(int[] dims)
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

        return newDims;
    }

    /*
    The goal of this function is to read in the entire affected region of the void
    and to return a 2d int array that has values 0 for normal spaces, -1 for voids,
    -2 for off map, and incremental numbers for virtual voids. If any of the spots
    are unknown, the function will return null since unknown spots mean we will not
    be able to do any more analysis.
     */
    private int[][] createVoidMap(int[] dims, int[] newDims, int[][] area)
    {

        int height = newDims[3] - newDims[1] + 1;
        int width = newDims[2] - newDims[0] + 1;

        int[][] newArea = new int[height][width];

        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                int currentX = a + newDims[0];
                int currentY = k + newDims[1];

                if(currentX >= dims[0] && currentX <= dims[2] &&
                   currentY >= dims[1] && currentY <= dims[3])
                {
                    newArea[k][a] = area[k - (dims[1] - newDims[1])][a - (dims[0] - newDims[0])];
                }
                else
                {
                    TerrainTile tile = rc.senseTerrainTile(new MapLocation(currentX, currentY));

                    if(tile == TerrainTile.NORMAL)
                    {
                        newArea[k][a] = 0;
                    }
                    else if(tile == TerrainTile.VOID)
                    {
                        newArea[k][a] = -1;
                    }
                    else if(tile == TerrainTile.OFF_MAP)
                    {
                        newArea[k][a] = -2;
                    }
                    else
                    {
                        //spot is unknown, so the function cannot finish
                        return null;
                    }
                }
            }
        }

        return newArea;
    }

    /*
    This method takes in the void feature and sets up virtual voids wherever
    there are bowls or inside corners so the bot will know to avoid them
    This will be accomplished by simply iterating through each diagonal,
    vertical, and horizontal line and seeing if any spots lie between two
    voids and marking them.
    This algorithm will also try to differentiate different virtual void areas,
    for example setting one area to 3 and another to 4 that are disconnected.
    The reason being that if our target is in a virtual void and we are in one,
    but not the right one, we know to go around and try from a different way.
    This will be accomplished by taking every void and setting all the ones next
    to it equal to that one and propagating it around until each group has its
    own number.
    There is one known bug which is that if there is another void that falls in
    the main void's rectangle, it may think there should be a virtual void.
     */
    private int[][] analyzeVirtualVoids(int[] dims)
    {
        int height = dims[3] - dims[1] + 1;
        int width = dims[2] - dims[0] + 1;

        int[][] voidArea = new int[height][width];

        //this is somewhat redundant as it was done higher up for a larger size, but seemed
        //more efficient than rediscovering the void barriers and creating a new array from that
        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                int currentX = a + dims[0];
                int currentY = k + dims[1];
                TerrainTile tile = rc.senseTerrainTile(new MapLocation(currentX, currentY));

                if(tile == TerrainTile.VOID)
                {
                    voidArea[k][a] = -1;
                }
                else if(tile == TerrainTile.UNKNOWN)
                {
                    return null;
                }
            }
        }

        //horizontal
        for(int k = 0; k < height; k++)
        {
            int start = 0;
            int end = width - 1;

            while(voidArea[k][start] != -1 && start < end)
            {
                start++;
            }
            while(voidArea[k][end] != -1 && start < end)
            { end--;
            }

            if(start != end)
            {
                for(int a = start + 1; a < end; a++)
                {
                    if(voidArea[k][a] != -1)
                    {
                        voidArea[k][a] = 1;
                    }
                }
            }
        }

        //vertical
        for(int k = 0; k < width; k++)
        {
            int start = 0;
            int end = height - 1;

            while(voidArea[start][k] != -1 && start < end)
            {
                start++;
            }
            while(voidArea[end][k] != -1 && start < end)
            {
                end--;
            }

            if(start != end)
            {
                for(int a = start + 1; a < end; a++)
                {
                    if(voidArea[a][k] != -1)
                    {
                        voidArea[a][k] = 1;
                    }
                }
            }
        }

        //diagonal \
        for(int k = height * -1 + 1; k < height; k++)
        {
            int start = 0;
            int end = width - 1;

            while((k + start < 0 || k + start >= height || voidArea[k + start][start] != -1) && start < end)
            {
                start++;
            }
            while((k + end < 0 || k + end >= height || voidArea[k + end][end] != -1) && start < end)
            {
                end--;
            }

            if(start != end)
            {
                for(int a = start + 1; a < end; a++)
                {
                    if(voidArea[k + a][a] != -1)
                    {
                        voidArea[k + a][a] = 1;
                    }
                }
            }
        }

        //diagonal /
        for(int k = height * -1 + 1; k < height; k++)
        {
            int start = 0;
            int end = width - 1;

            while((height - 1 - (k + start) < 0 || height - 1 - (k + start) >= height ||
                   voidArea[height - 1 - (k + start)][start] != -1) && start < end)
            {
                start++;
            }
            while((height - 1 - (k + end) < 0 || height - 1 - (k + end) >= height ||
                   voidArea[height - 1 - (k + end)][end] != -1) && start < end)
            {
                end--;
            }

            if(start != end)
            {
                for(int a = start + 1; a < end; a++)
                {
                    if(voidArea[height - 1 - (k + a)][a] != -1)
                    {
                        voidArea[height - 1 - (k + a)][a] = 1;
                    }
                }
            }
        }

        int number = 10;
        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                if(voidArea[k][a] == 1)
                {
                    voidArea = recursiveChangeNumber(voidArea, number, k, a);
                    number++;
                }
            }
        }

        return voidArea;
    }

    private int[][] recursiveChangeNumber(int[][] area, int number, int x, int y)
    {
        area[x][y] = number;
        for(int k = -1; k <= 1; k++)
        {
            for(int a = -1; a <= 1; a++)
            {
                if(x + k >= 0 && x + k < area.length &&
                   y + a >= 0 && y + a < area[0].length)
                {
                    if(area[x + k][y + a] == 1)
                    {
                        area = recursiveChangeNumber(area, number, x + k, y + a);
                    }
                }
            }
        }

        return area;
    }
}
