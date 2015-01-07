package theSimpleSoldier.Units;

import battlecode.world.Util;
import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;

import battlecode.common.*;
import theSimpleSoldier.Utilities;

public class Tank extends Unit
{
    Navigator nav;
    MapLocation target;
    FightMicro fighter;
    RobotInfo[] nearByEnemies;
    int range;
    public Tank(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
        fighter = new FightMicro(rc);
        range = rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
        target = Utilities.getTowerClosestToEnemyHQ(rc);
    }

    public void collectData()
    {
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
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
        if (nearByEnemies.length > 0)
        {
            return false;
        }
        return nav.takeNextStep(target);
        //return nav.badMovement(target);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basicFightMicro(nearByEnemies);
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
