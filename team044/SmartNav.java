package team044;

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

        if(isPosted(dims))
        {
            return false;
        }

        int[][] area = analyzeVirtualVoids(dims);

        if(area == null)
        {
            return false;
        }

        rc.yield();
        if(isPosted(dims))
        {
            return false;
        }

        post(area, dims);

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
    The current solution to it is to go through the map again in each direction
    and when there is a full path through, clear it out.
     */
    private int[][] analyzeVirtualVoids(int[] dims)
    {
        boolean shouldPost = false;
        int height = dims[3] - dims[1] + 1;
        int width = dims[2] - dims[0] + 1;

        int[][] voidArea = new int[height][width];

        int max = Math.max(height, width);

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
                        shouldPost = true;
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
                        shouldPost = true;
                    }
                }
            }
        }

        //diagonal \
        for(int k = max * -1 + 1; k < height; k++)
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
                        shouldPost = true;
                    }
                }
            }
        }

        //diagonal /
        for(int k = 0; k < max * 2; k++)
        {
            int start = 0;
            int end = width - 1;

            while((k - start < 0 || k - start >= height ||
                   voidArea[k - start][start] != -1) && start < end)
            {
                start++;
            }
            while((k - end < 0 || k - end >= height ||
                   voidArea[k - end][end] != -1) && start < end)
            {
                end--;
            }

            if(start != end)
            {
                for(int a = start + 1; a < end; a++)
                {
                    if(voidArea[k - a][a] != -1)
                    {
                        voidArea[k - a][a] = 1;
                        shouldPost = true;
                    }
                }
            }
        }

        if(!shouldPost)
        {
            return null;
        }

        //The following four break holes in the voids where there shouldn't be one
        //This is an attempt to fix the bug mentioned at the beginning of the method
        //horizontal
        for(int k = 0; k < height; k++)
        {
            int start = 0;

            while(start < width && voidArea[k][start] != -1)
            {
                start++;
            }
            if(start == width)
            {
                start = 0;
                while(start < width && voidArea[k][start] != -1)
                {
                    voidArea[k][start] = 0;
                    start++;
                }
            }
        }

        //vertical
        for(int k = 0; k < width; k++)
        {
            int start = 0;

            while(start < height && voidArea[start][k] != -1)
            {
                start++;
            }
            if(start == height)
            {
                start = 0;
                while(start < height && voidArea[start][k] != -1)
                {
                    voidArea[start][k] = 0;
                    start++;
                }
            }
        }

        //diagonal \
        for(int k = max * -1 + 1; k < height; k++)
        {
            int start = 0;

            while(start < width && (k + start < 0 || k + start >= height || voidArea[k + start][start] != -1))
            {
                start++;
            }
            if(start == width)
            {
                start = 0;
                while(start < width && (k + start < 0 || k + start >= height || voidArea[k + start][start] != -1))
                {
                    if(k + start >= 0 && k + start < height)
                    {
                        voidArea[k + start][start] = 0;
                    }
                    start++;
                }
            }
        }

        //diagonal /
        for(int k = 0; k < max * 2; k++)
        {
            int start = 0;

            while(start < width && (k - start < 0 || k - start >= height || voidArea[k - start][start] != -1))
            {
                start++;
            }
            if(start == width)
            {
                start = 0;
                while(start < width && (k - start < 0 || k - start >= height ||
                                        voidArea[k - start][start] != -1))
                {
                    if(k - start >= 0 && k - start < height)
                    {
                        voidArea[k - start][start] = 0;
                    }
                    start++;
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

    /*
    This function posts a fully created area to the broadcast channels and fills in
    header information. The channels will be organized as follows.
    The first channel is the next spot header information should go.
    The next channel is the next spot area information should go.
    The next 1000 channels store header information, which uses 5 channels each.
    1 is start channel for area, next is start x, next start y, then width, and last
    is height.
    The area information is just all the values, stored in a linear order, but easy
    to parse knowing the dimensions of the area.
     */
    private void post(int[][] area, int[] dims) throws GameActionException
    {
        int startChannel = Constants.startNavChannels;
        int nextHeader = rc.readBroadcast(startChannel);
        int nextArea = rc.readBroadcast(startChannel + 1);

        if(nextHeader == 0)
        {
            nextHeader = startChannel + 2;
        }
        if(nextArea == 0)
        {
            nextArea = startChannel + 2 + 1000;
        }

        int width = dims[2] - dims[0];
        int height = dims[3] - dims[1];

        rc.broadcast(nextHeader, nextArea);
        rc.broadcast(nextHeader + 1, dims[0]);
        rc.broadcast(nextHeader + 2, dims[1]);
        rc.broadcast(nextHeader + 3, width);
        rc.broadcast(nextHeader + 4, height);
        rc.broadcast(startChannel, nextHeader + 5);
        rc.broadcast(startChannel + 1, nextArea + height * width);

        int counter = 0;
        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                rc.broadcast(nextArea + counter, area[k][a]);
                counter++;
            }
        }
    }

    private boolean isPosted(int[] dims) throws GameActionException
    {
        int startChannel = Constants.startNavChannels;
        int lastHeader = rc.readBroadcast(startChannel);

        int width = dims[2] - dims[0];
        int height = dims[3] - dims[1];

        for(int k = 0; k < lastHeader; k += 5)
        {
            if(rc.readBroadcast(startChannel + 2 + k + 1) == dims[0] &&
               rc.readBroadcast(startChannel + 2 + k + 2) == dims[1] &&
               rc.readBroadcast(startChannel + 2 + k + 3) == width &&
               rc.readBroadcast(startChannel + 2 + k + 4) == height)
            {
                return true;
            }
        }

        return false;
    }
}
