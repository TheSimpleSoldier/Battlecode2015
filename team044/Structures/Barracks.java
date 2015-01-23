package team044.Structures;

import battlecode.common.*;
import team044.Messaging;
import team044.Structure;
import team044.Utilities;

public class Barracks extends Structure
{
    boolean basher = true;
    int numbOfSoldiers = 0;
    int counter = 0;
    public Barracks(RobotController rc)
    {
        super(rc);
        if (rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) > 35)
        {
            basher = true;
        }
        rc.setIndicatorString(0, "Barracks");
    }

    // overridden methods go here

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();
        numbOfSoldiers = rc.readBroadcast(Messaging.NumbOfSoldiers.ordinal());
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (Clock.getRoundNum() > 0 && Utilities.spawnUnit(RobotType.BASHER, rc))
        {
            return true;
        }

        /*
        if (rc.readBroadcast(Messaging.ShutOffBasherProd.ordinal()) == 0)
        {
            if (Utilities.spawnUnit(RobotType.BASHER, rc))
            {
                counter++;
                return true;
            }
        }
        else if (rc.readBroadcast(Messaging.ShutOffSoldierProd.ordinal()) == 0)
        {
            if (Utilities.spawnUnit(RobotType.SOLDIER, rc))
            {
                counter++;
                return true;
            }
        }
        */
        return false;
    }
}
