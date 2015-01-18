package _teamlauncherBasher.Units.Rushers;

import battlecode.common.*;
import _teamlauncherBasher.Messaging;
import _teamlauncherBasher.Unit;
import _teamlauncherBasher.Units.Basher;
import _teamlauncherBasher.Utilities;

public class BasherRusher extends Basher
{
    public BasherRusher(RobotController rc)
    {
        super(rc);

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
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
