package theSimpleSoldier.Units;

import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;

import battlecode.common.*;
import theSimpleSoldier.Utilities;

public class Miner extends Unit
{
    Navigator nav;
    FightMicro fighter;
    MapLocation target;
    RobotInfo[] nearByEnemies;

    public Miner(RobotController rc)
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
        if (rc.senseOre(rc.getLocation()) < 5)
        {
            //target = Utilities.getBestMiningSpot(rc);
            target = Utilities.greedyBestMiningSpot(rc);
        }
    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }

        //nav.takeNextStep(target);
        return nav.badMovement(target);
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
        if (rc.isCoreReady() && rc.canMine() && rc.senseOre(rc.getLocation()) >= 1)
        {
            rc.mine();
            return true;
        }

        return false;
    }
}
