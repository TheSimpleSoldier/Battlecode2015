package _teamdroneSurround.Structures;

import battlecode.common.*;
import _teamdroneSurround.Messaging;
import _teamdroneSurround.Structure;
import _teamdroneSurround.Utilities;

public class TechnologyInstitute extends Structure {
    int numbComputers;
    public TechnologyInstitute(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        rc.broadcast(Messaging.ComputerOnline.ordinal(), 0);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (numbComputers == 0 && !Utilities.cutProd(rc) && Utilities.spawnUnit(RobotType.COMPUTER, rc))
        {
            numbComputers = 1;
            return true;
        }

        return false;
    }
}
