package _teamSeeding.Units;


import _teamSeeding.*;

import battlecode.common.*;
import _teamSeeding.Units.Defenders.DefensiveBasher;
import _teamSeeding.Units.Rushers.BasherRusher;
import _teamSeeding.Units.SquadUnits.BasherSquad;
import _teamSeeding.Units.harrassers.BasherHarrass;

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
        int x = rc.readBroadcast(Messaging.CommanderLocX.ordinal());
        int y = rc.readBroadcast(Messaging.CommanderLocY.ordinal());

        if (x != 0 && y != 0)
        {
            target = new MapLocation(x, y);
        }
        else
        {
            target = ourHQ.add(enemyHQ.directionTo(ourHQ), 3);
        }
        rc.setIndicatorString(1, "Target: " + target);
        /*MapLocation[] enemyTower = rc.senseEnemyTowerLocations();
        if (enemyTower.length > 0)
        {
            target = enemyTower[0];
        }
        else
        {
            target = rc.senseEnemyHQLocation();
        }*/
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
        int type = rc.readBroadcast(Messaging.BasherType.ordinal());
        rc.broadcast(Messaging.BasherType.ordinal(), -1);

        if (type == BuildOrderMessaging.BuildDefensiveBasher.ordinal())
        {
            return new DefensiveBasher(rc);
        }
        else if (type == BuildOrderMessaging.BuildHarrassBasher.ordinal())
        {
            return new BasherHarrass(rc);
        }
        else if (type == BuildOrderMessaging.BuildSquadBasher.ordinal())
        {
            return new BasherSquad(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
