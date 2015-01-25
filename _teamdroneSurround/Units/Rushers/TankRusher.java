package _teamdroneSurround.Units.Rushers;

import battlecode.common.*;

import _teamdroneSurround.Unit;
import _teamdroneSurround.Units.Tank;
import _teamdroneSurround.Utilities;

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
