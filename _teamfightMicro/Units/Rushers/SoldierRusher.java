package _teamfightMicro.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro.Unit;
import _teamfightMicro.Units.Soldier;
import _teamfightMicro.Utilities;

public class SoldierRusher extends Soldier
{
    public SoldierRusher(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        target = Utilities.getRushLocation(rc);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }
        return nav.takeNextStep(target);
    }
}
