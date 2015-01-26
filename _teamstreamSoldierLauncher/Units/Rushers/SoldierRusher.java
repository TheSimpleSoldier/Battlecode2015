package _teamstreamSoldierLauncher.Units.Rushers;

import battlecode.common.*;
import _teamstreamSoldierLauncher.Unit;
import _teamstreamSoldierLauncher.Units.Soldier;
import _teamstreamSoldierLauncher.Utilities;

public class SoldierRusher extends Soldier
{
    public SoldierRusher(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        RobotInfo[] allies = rc.senseNearbyRobots(24, us);

        target = Utilities.getRushLocation(rc);

        if (allies.length > 4 || rc.getLocation().distanceSquaredTo(target) > 48)
        {
            // just keep advancing
            nav.setAvoidTowers(false);
            nav.setAvoidHQ(false);
        }
        else if (allies.length > 0)
        {
            nav.setAvoidTowers(true);
            nav.setAvoidHQ(true);
            int closestToTower = 999999;
            MapLocation closestAlly = target;

            for (int i = allies.length; --i>=0; )
            {
                MapLocation ally = allies[i].location;
                int dist = ally.distanceSquaredTo(target);
                if (dist < closestToTower)
                {
                    closestToTower = dist;
                    closestAlly = ally;
                }
            }

            target = closestAlly;
        }
        else
        {
            nav.setAvoidTowers(true);
            nav.setAvoidHQ(true);
        }
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
