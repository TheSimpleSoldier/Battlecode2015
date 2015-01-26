package _teambasherSoldier.Units.Rushers;

import battlecode.common.*;

import _teambasherSoldier.Unit;
import _teambasherSoldier.Units.Tank;
import _teambasherSoldier.Utilities;

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
