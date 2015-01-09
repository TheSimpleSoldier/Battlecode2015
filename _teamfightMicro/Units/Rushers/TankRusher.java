package _teamfightMicro.Units.Rushers;

import battlecode.common.*;

import _teamfightMicro.Unit;
import _teamfightMicro.Units.Tank;
import _teamfightMicro.Utilities;

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
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }
}
