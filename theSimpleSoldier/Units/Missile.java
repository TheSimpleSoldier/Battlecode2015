package theSimpleSoldier.Units;


import battlecode.common.*;
import theSimpleSoldier.FightMicro;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;

public class Missile extends Unit
{
    FightMicro fighter;
    Navigator nav;
    public Missile(RobotController rc)
    {
        this.rc = rc;
        fighter = new FightMicro(rc);
        nav = new Navigator(rc);
    }

    public void collectData() throws GameActionException
    {

    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
    }

    public boolean takeNextStep() throws GameActionException
    {
        return false;
    }

    public boolean fight() throws GameActionException
    {
        MapLocation target = fighter.missileAttack2();
        nav.badMovement(target);
        return true;
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }

    public void distributeSupply() throws GameActionException
    {
        // can't afford to waste bytecodes with supplies
    }
}
