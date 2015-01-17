package TestNav;

/**
 * Created by joshua on 1/17/15.
 */
public class MyDirection
{
    public int dir;

    public MyDirection(int dir)
    {
        this.dir = dir;
    }

    public void turnRight()
    {
        dir = (dir + 1) % 8;
    }

    public void turnLeft()
    {
        dir = (dir + 7) % 8;
    }

    public int[] goThisDir(int[] location)
    {
        int[] toReturn = {location[0], location[1]};
        if(dir == Directions.North.ordinal())
        {
            toReturn[1]--;
        }
        if(dir == Directions.NorthEast.ordinal())
        {
            toReturn[1]--;
            toReturn[0]++;
        }
        if(dir == Directions.East.ordinal())
        {
            toReturn[0]++;
        }
        if(dir == Directions.SouthEast.ordinal())
        {
            toReturn[1]++;
            toReturn[0]++;
        }
        if(dir == Directions.South.ordinal())
        {
            toReturn[1]++;
        }
        if(dir == Directions.SouthWest.ordinal())
        {
            toReturn[1]++;
            toReturn[0]--;
        }
        if(dir == Directions.West.ordinal())
        {
            toReturn[0]--;
        }
        if(dir == Directions.NorthWest.ordinal())
        {
            toReturn[1]--;
            toReturn[0]--;
        }

        return toReturn;
    }
}
