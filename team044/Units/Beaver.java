package team044.Units;

import team044.*;
import battlecode.common.*;
import team044.Units.Rushers.BasherRusher;
import team044.Units.Rushers.BeaverRusher;

public class Beaver extends Unit
{
    int buildingType;

    public Beaver()
    {
        // we are in trouble
    }

    public Beaver(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
    }

    public void handleMessages() throws GameActionException
    {
        Utilities.handleMessageCounter(rc, Messaging.NumbOfBeaverOdd.ordinal(), Messaging.NumbOfBeaverEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }
        rc.setIndicatorString(0, "In navigation: " + target);
        return nav.takeNextStep(target);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basicFightMicro(nearByEnemies);
        //return fighter.basicFightMicro(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new BeaverRusher(rc);
        }
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
