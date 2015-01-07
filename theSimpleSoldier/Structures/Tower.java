package theSimpleSoldier.Structures;

import battlecode.common.*;
import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Messaging;
import theSimpleSoldier.Structure;

public class Tower extends Structure
{
    FightMicro fighter;
    RobotInfo[] enemies;
    RobotInfo[] nearByEnemies;
    RobotInfo[] allies;
    RobotInfo[] nearByAllies;
    public Tower(RobotController rc)
    {
        this.rc = rc;
        fighter = new FightMicro(rc);
        us = rc.getTeam();
        opponent = us.opponent();
        range = rc.getType().attackRadiusSquared;
    }

    public void handleMessages() throws GameActionException
    {
        if (nearByEnemies.length > 0)
        {
            MapLocation[] towers = rc.senseTowerLocations();
            MapLocation us = rc.getLocation();

            for (int i = 0; i < towers.length; i++)
            {
                if (towers[i] == us)
                {
                    rc.broadcast(Messaging.TowerUnderAttack.ordinal(), i);
                }
            }
        }
    }

    public void collectData() throws GameActionException
    {
        enemies = rc.senseNearbyRobots(99999, opponent);
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
        allies = rc.senseNearbyRobots(99999, us);
        nearByAllies = rc.senseNearbyRobots(range, us);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.structureFightMicro(nearByEnemies);
    }

}
