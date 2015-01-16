package team044.Units;


import team044.*;

import battlecode.common.*;
import team044.Units.Rushers.BasherRusher;

import javax.rmi.CORBA.Util;

public class Basher extends Unit
{
    public Basher(RobotController rc)
    {
        super(rc);

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        
        // collect our data
        //target = Utilities.getRushLocation(rc);

        target = Utilities.getTowerClosestToEnemyHQ(rc);
        if (target == null)
        {
            target = ourHQ.add(ourHQ.directionTo(enemyHQ), 10);
        }

        rc.setIndicatorString(1, "Target: " + target);

    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfBashersOdd.ordinal(), Messaging.NumbOfBashersEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        return nav.takeNextStep(target);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basherFightMicro();
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new BasherRusher(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
