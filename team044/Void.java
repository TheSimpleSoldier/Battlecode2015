package team044;

import battlecode.common.MapLocation;

/**
 * Created by joshua on 1/18/15.
 */
public class Void
{
    public int[][] voidMap;
    public int startX, endX, startY, endY, width, height;

    public Void(int[][] voidMap, int startX, int startY, int width, int height)
    {
        this.voidMap = voidMap;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        endX = startX + width - 1;
        endY = startY + height - 1;
    }

    public int getSpotValue(MapLocation location)
    {
        if(location.x >= startX && location.x <= endX &&
           location.y >= startY && location.y <= endY)
        {
            return voidMap[location.y - startY][location.x - startX];
        }

        //-3 represents an error
        return -3;
    }
}
