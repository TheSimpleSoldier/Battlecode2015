package theSimpleSoldier.Units;

import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;

import battlecode.common.*;

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
    }

    public void collectData()
    {
        target = rc.senseEnemyHQLocation();
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
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
