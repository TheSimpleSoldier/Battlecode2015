package theSimpleSoldier;

import battlecode.common.*;

/**
 * This class is for common behaviou
 */
public abstract class Structure extends Unit
{
    public void collectData()
    {
        // collect our data
    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
    }

    // structures can't move!
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
