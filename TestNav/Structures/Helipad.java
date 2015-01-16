package TestNav.Structures;

import battlecode.common.*;
import TestNav.Messaging;
import TestNav.Structure;
import TestNav.Utilities;

public class Helipad extends Structure
{
    int numbOfDrones;
    boolean spawned;
    public Helipad(RobotController rc)
    {
        super(rc);
        spawned = false;
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();
        numbOfDrones = rc.readBroadcast(Messaging.NumbOfDrones.ordinal());
    }

    public boolean carryOutAbility() throws GameActionException
    {
        // start by only keeping up at most one drone at a time
        if (!spawned && Utilities.spawnUnit(RobotType.DRONE, rc))
        {
            //spawned = true;
            return true;
        }
        return false;
    }
}
