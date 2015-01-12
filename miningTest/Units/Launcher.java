package miningTest.Units;


import miningTest.*;

import battlecode.common.*;
import miningTest.Units.Rushers.LauncherRusher;

public class Launcher extends Unit
{
    public Launcher(RobotController rc)
    {
        super(rc);
        // override supers range
        range = 24;

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
        // collect our data

        target = Utilities.getTowerClosestToEnemyHQ(rc);

        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
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

        nearByEnemies = rc.senseNearbyRobots(range, opponent);
    }

    public void handleMessages() throws GameActionException
    {
        // if we are getting low on supply and are near other robots send out request
        if (rc.getSupplyLevel() < 40 && nearByAllies.length > 1)
        {
            MapLocation mySpot = rc.getLocation();
            rc.broadcast(Messaging.FirstNeedSupplyX.ordinal(), mySpot.x);
            rc.broadcast(Messaging.FirstNeedSupplyY.ordinal(), mySpot.y);
        }

        Utilities.handleMessageCounter(rc, Messaging.NumbOfLaunchersOdd.ordinal(), Messaging.NumbOfLaunchersEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (nearByEnemies.length > 0)
        {
            return false;
        }
        else if (Utilities.nearEnemyTower(rc))
        {
            return false;
        }
        else
        {
            return nav.takeNextStep(target);
        }
    }

    public boolean fight() throws GameActionException
    {
        return fighter.launcherAttack(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new LauncherRusher(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
