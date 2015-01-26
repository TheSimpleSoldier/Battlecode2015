package _teamstreamLaunchers.Structures;

import battlecode.common.*;
import _teamstreamLaunchers.Messaging;
import _teamstreamLaunchers.Structure;
import _teamstreamLaunchers.Utilities;

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
        if (numbOfDrones < 1 && !Utilities.cutProd(rc) && Utilities.spawnUnit(RobotType.DRONE, rc))
        {
            return true;
        }
        return false;
    }
}
