package _teamlauncherBasher.Units.Rushers;

import battlecode.common.*;
import _teamlauncherBasher.Unit;
import _teamlauncherBasher.Units.Soldier;
import _teamlauncherBasher.Utilities;

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
