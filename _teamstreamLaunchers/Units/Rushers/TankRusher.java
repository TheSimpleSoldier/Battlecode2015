package _teamstreamLaunchers.Units.Rushers;

import battlecode.common.*;

import _teamstreamLaunchers.Unit;
import _teamstreamLaunchers.Units.Tank;
import _teamstreamLaunchers.Utilities;

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
