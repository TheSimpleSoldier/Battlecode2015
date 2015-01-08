package team044;

import battlecode.common.*;

public abstract class Unit
{
    public RobotController rc;
    public int range;
    public Team us;
    public Team opponent;
    MapLocation ourHQ;
    MapLocation enemyHQ;

    public abstract void collectData() throws GameActionException;

    public void handleMessages() throws GameActionException
    {
        // if we are getting low on supply send out request
        if (rc.getSupplyLevel() < 40)
        {
            if (rc.readBroadcast(Messaging.FirstNeedSupplyX.ordinal()) == 0)
            {
                MapLocation mySpot = rc.getLocation();
                rc.broadcast(Messaging.FirstNeedSupplyX.ordinal(), mySpot.x);
                rc.broadcast(Messaging.FirstNeedSupplyY.ordinal(), mySpot.y);
            }
            else if (rc.readBroadcast(Messaging.SecondNeedSupplyX.ordinal()) == 0)
            {
                MapLocation mySpot = rc.getLocation();
                rc.broadcast(Messaging.SecondNeedSupplyX.ordinal(), mySpot.x);
                rc.broadcast(Messaging.SecondNeedSupplyY.ordinal(), mySpot.y);
            }
        }



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

    public void distributeSupply() throws GameActionException
    {
        //Utilities.shareSupplies(rc);
    }
}
