package theSimpleSoldier;

import battlecode.common.*;

/**
 * This class is for common behaviou
 */
public abstract class Structure extends Unit
{
    public void collectData() throws GameActionException
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

    // for structures even distribute supplies among all allies
    public void distributeSupply() throws  GameActionException
    {
        int dist = GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED - 1;

        if (rc == null)
        {
            System.out.println("Houston we have a serious problem");
        }

        RobotInfo[] closeAllies = rc.senseNearbyRobots(dist, us);
        if (closeAllies.length <= 0)
        {
            return;
        }

        int supplies = (int) (rc.getSupplyLevel() / closeAllies.length);


        for (int i = 0; i < closeAllies.length; i++)
        {
            if (rc.senseRobotAtLocation(closeAllies[i].location) != null)
            {
                rc.transferSupplies(supplies, closeAllies[i].location);
            }
        }
    }
}
