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
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        
        // collect our data
        MapLocation[] enemyTower = rc.senseEnemyTowerLocations();
        if (enemyTower.length > 0)
        {
            target = enemyTower[0];
        }
        else
        {
            target = rc.senseEnemyHQLocation();
        }
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
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
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
