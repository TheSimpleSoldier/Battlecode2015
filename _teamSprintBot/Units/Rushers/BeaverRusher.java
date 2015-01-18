package _teamSprintBot.Units.Rushers;

import battlecode.common.*;
import _teamSprintBot.Unit;
import _teamSprintBot.Units.Beaver;
import _teamSprintBot.Utilities;

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
