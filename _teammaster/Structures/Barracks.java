package _teammaster.Structures;

import battlecode.common.*;
import _teammaster.Messaging;
import _teammaster.Structure;
import _teammaster.Utilities;

public class Barracks extends Structure
{
    boolean basher = true;
    int numbOfSoldiers = 0;
    public Barracks(RobotController rc)
    {
        super(rc);
        if (rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) > 35)
        {
            basher = false;
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
        /*
        if (basher)
        {
            if (rc.getTeamOre() > 600 && Utilities.spawnUnit(RobotType.BASHER, rc))
            {
                return true;
            }
        }
        else
        {
            if (numbOfSoldiers < 10 && Utilities.spawnUnit(RobotType.SOLDIER, rc))
            {
                return true;
            }
        }*/
        return false;
    }
}
