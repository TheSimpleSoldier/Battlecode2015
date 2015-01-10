package _teamfightMicro2.Units;


import _teamfightMicro2.*;

import battlecode.common.*;
import _teamfightMicro2.Units.Rushers.BasherRusher;

import javax.rmi.CORBA.Util;

public class Basher extends Unit
{
    public MapLocation target;

    public Basher(RobotController rc)
    {
        super(rc);
        target = Utilities.getTowerClosestToEnemyHQ(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        // collect our data
        MapLocation[] enemyTower = rc.senseEnemyTowerLocations();
        if (Clock.getRoundNum() > 1000 && enemyTower.length > 0)
        {
            target = enemyTower[0];
        }
        else if (Clock.getRoundNum() > 1500)
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
        return false;
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
