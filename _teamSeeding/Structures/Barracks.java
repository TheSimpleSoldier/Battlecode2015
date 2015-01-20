package _teamSeeding.Structures;

import battlecode.common.*;
import _teamSeeding.Messaging;
import _teamSeeding.Structure;
import _teamSeeding.Utilities;

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
        return false;
    }
}
