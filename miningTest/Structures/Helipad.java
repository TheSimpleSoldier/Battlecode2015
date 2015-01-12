package miningTest.Structures;

import battlecode.common.*;
import miningTest.Messaging;
import miningTest.Structure;
import miningTest.Utilities;

public class Helipad extends Structure
{
    int numbOfDrones;
    public Helipad(RobotController rc)
    {
        super(rc);
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
        if (numbOfDrones < 20 && Utilities.spawnUnit(RobotType.DRONE, rc))
        {
            return true;
        }
        return false;
    }
}
