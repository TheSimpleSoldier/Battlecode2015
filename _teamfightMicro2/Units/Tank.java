package _teamfightMicro2.Units;

import _teamfightMicro2.*;

import battlecode.common.*;
import _teamfightMicro2.Units.Rushers.TankRusher;

public class Tank extends Unit
{
    public MapLocation target;

    public Tank()
    {
        // Houston we have a problem
    }

    public Tank(RobotController rc)
    {
        super(rc);
        target = Utilities.getTowerClosestToEnemyHQ(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        MapLocation[] enemyTower = rc.senseEnemyTowerLocations();
        if (Clock.getRoundNum() > 1000 && enemyTower.length > 0)
        {
            target = enemyTower[0];
        }
        else if (Clock.getRoundNum() > 1500)
        {
            target = rc.senseEnemyHQLocation();
        }

        if (target == null)
        {
            target = rc.senseEnemyHQLocation();
        }
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfTanksOdd.ordinal(), Messaging.NumbOfTanksEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (nearByEnemies.length > 0)
        {
            return false;
        }
        int byteCodes = Clock.getBytecodeNum();
        int roundNumb = Clock.getRoundNum();
        boolean move = nav.takeNextStep(target);
        byteCodes = Clock.getBytecodeNum() - byteCodes;
        roundNumb = Clock.getRoundNum() - roundNumb;
        if (roundNumb > 0)
        {
            //System.out.println("Byte Codes: " + byteCodes + ", Rounds: " + roundNumb);
        }
        return move;
    }

    public boolean fight() throws GameActionException
    {
        return fighter.advancedFightMicro(nearByEnemies);
        //return fighter.basicFightMicro(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new TankRusher(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
