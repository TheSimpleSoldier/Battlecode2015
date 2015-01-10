package _teamfightMicro2.Units.Rushers;

import battlecode.common.*;

import _teamfightMicro2.Unit;
import _teamfightMicro2.Units.Tank;
import _teamfightMicro2.Utilities;

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
