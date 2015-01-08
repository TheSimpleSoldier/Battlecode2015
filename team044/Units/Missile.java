package team044.Units;


import battlecode.common.*;
import team044.FightMicro;
import team044.Navigator;
import team044.Unit;

public class Missile extends Unit
{
    FightMicro fighter;
    Navigator nav;
    public Missile(RobotController rc)
    {
        // to save bytecodes we don't use constructor supplied by Unit
        this.rc = rc;
        fighter = new FightMicro(rc);
        nav = new Navigator(rc);
    }

    public void collectData() throws GameActionException
    {
        // do nothing to save on bytecodes
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
