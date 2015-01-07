package team044.Units;

import team044.FightMicro;
import team044.Navigator;
import team044.Unit;
import battlecode.common.*;

public class Beaver extends Unit
{
    Navigator nav;
    FightMicro fighter;
    int buildingType;
    MapLocation target;
    RobotInfo[] nearByEnemies;
    public Beaver()
    {
        // we are in trouble
    }

    public Beaver(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
        fighter = new FightMicro(rc);
        range = rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
    }

    public void handleMessages() throws GameActionException
    {
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
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
        if (rc.isCoreReady() && rc.canMine() && rc.senseOre(rc.getLocation()) >= 2)
        {
            rc.mine();
            return true;
        }

        return false;
    }
}
