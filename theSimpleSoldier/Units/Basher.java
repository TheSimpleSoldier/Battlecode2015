package theSimpleSoldier.Units;


import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;

import battlecode.common.*;

public class Basher extends Unit
{
    FightMicro fighter;
    MapLocation target;
    Navigator nav;
    public Basher(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
        fighter = new FightMicro(rc);
        range = rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
        target = rc.senseTowerLocations()[0];
    }

    public void collectData()
    {
        // collect our data
        MapLocation[] enemyTower = rc.senseEnemyTowerLocations();
        if (Clock.getRoundNum() > 1000 && enemyTower.length > 0)
        {
            target = enemyTower[0];
        }
        else if (Clock.getRoundNum() > 1500)
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
        return nav.badMovement(target);
    }

    public boolean fight() throws GameActionException
    {
        return false;
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
