package _teamfightMicro2.Units.Rushers;

import battlecode.common.*;
import _teamfightMicro2.Messaging;
import _teamfightMicro2.Unit;
import _teamfightMicro2.Units.Basher;
import _teamfightMicro2.Utilities;

public class BasherRusher extends Basher
{
    public BasherRusher(RobotController rc)
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
