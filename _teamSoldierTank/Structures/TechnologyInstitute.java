package _teamSoldierTank.Structures;

import battlecode.common.*;
import _teamSoldierTank.Messaging;
import _teamSoldierTank.Structure;
import _teamSoldierTank.Utilities;

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
