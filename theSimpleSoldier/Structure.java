package theSimpleSoldier;

import battlecode.common.*;

/**
 * This class is for common behaviou
 */
public abstract class Structure extends Unit
{
    public RobotInfo[] nearByEnemies;
    public void collectData() throws GameActionException
    {
        // collect our data
        nearByEnemies = rc.senseNearbyRobots(24, rc.getTeam().opponent());
    }

    public void handleMessages() throws GameActionException
    {
        if (nearByEnemies.length > 0)
        {
            rc.broadcast(Messaging.BuildingInDistressX.ordinal(), rc.getLocation().x);
            rc.broadcast(Messaging.BuildingInDistressY.ordinal(), rc.getLocation().y);
        }
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

        if (rc.getSupplyLevel() == 0)
        {
            return;
        }

        if (rc == null)
        {
            System.out.println("Houston we have a serious problem");
        }
        rc.setIndicatorString(1, "");
        rc.setIndicatorString(2, "");

        RobotInfo[] closeAllies = rc.senseNearbyRobots(dist, us);
        if (closeAllies.length <= 0)
        {
            return;
        }

        int supplies = (int) (rc.getSupplyLevel() / closeAllies.length);


        for (int i = 0; i < closeAllies.length; i++)
        {
            if (Clock.getBytecodeNum() > 1750)
            {
                break;
            }
            MapLocation ally = closeAllies[i].location;
            if (rc.senseRobotAtLocation(ally) != null && rc.getLocation().distanceSquaredTo(ally) < dist)
            {
                rc.transferSupplies(supplies, ally);
            }
        }
    }
}
