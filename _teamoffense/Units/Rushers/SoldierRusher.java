package _teamoffense.Units.Rushers;

import battlecode.common.*;
import _teamoffense.Unit;
import _teamoffense.Units.Soldier;
import _teamoffense.Utilities;

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

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
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
