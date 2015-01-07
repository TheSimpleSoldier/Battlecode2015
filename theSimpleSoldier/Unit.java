package theSimpleSoldier;

import battlecode.common.*;

public abstract class Unit
{
    public RobotController rc;
    public int range;
    public Team us;
    public Team opponent;
    public abstract void collectData() throws GameActionException;

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
