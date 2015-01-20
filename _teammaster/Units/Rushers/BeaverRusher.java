package _teammaster.Units.Rushers;

import battlecode.common.*;
import _teammaster.Unit;
import _teammaster.Units.Beaver;
import _teammaster.Utilities;

public class BeaverRusher extends Beaver
{
    public BeaverRusher(RobotController rc)
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

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
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
