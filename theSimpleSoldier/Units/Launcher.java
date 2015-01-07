package theSimpleSoldier.Units;


import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;

import battlecode.common.*;

public class Launcher extends Unit
{
    Navigator nav;
    FightMicro fighter = new FightMicro(rc);
    MapLocation target;
    RobotInfo[] nearByEnemies;
    public Launcher(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
        fighter = new FightMicro(rc);
        range = 24;//rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
        target = rc.senseTowerLocations()[0];
    }

    public void collectData()
    {
        // collect our data
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
        MapLocation[] enemyTower = rc.senseEnemyTowerLocations();
        if (enemyTower.length > 0)
        {
            target = enemyTower[0];
        }
        else
        {
            target = rc.senseEnemyHQLocation();
        }
    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (rc.getLocation().distanceSquaredTo(target) > 35)
        {
            return nav.badMovement(target);
        }
        return false;
    }

    public boolean fight() throws GameActionException
    {
        return fighter.launcherAttack(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
