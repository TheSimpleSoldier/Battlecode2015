package _teamlauncherBasher.Structures;

import battlecode.common.*;
import _teamlauncherBasher.Messaging;
import _teamlauncherBasher.Structure;
import _teamlauncherBasher.Utilities;

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
        // start by only keeping up at most 5 drones at a time
        if (numbOfDrones < 5 && !Utilities.cutProd(rc) && Utilities.spawnUnit(RobotType.DRONE, rc))
        {
            return true;
        }
        return false;
    }
}
