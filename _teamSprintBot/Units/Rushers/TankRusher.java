package _teamSprintBot.Units.Rushers;

import battlecode.common.*;

import _teamSprintBot.Unit;
import _teamSprintBot.Units.Tank;
import _teamSprintBot.Utilities;

public class TankRusher extends Tank
{
    public TankRusher(RobotController rc)
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
}
