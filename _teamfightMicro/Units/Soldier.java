package _teamfightMicro.Units;

import battlecode.world.Util;
import _teamfightMicro.Messaging;
import _teamfightMicro.Navigator;
import _teamfightMicro.Unit;

import battlecode.common.*;
import _teamfightMicro.Units.Rushers.SoldierRusher;
import _teamfightMicro.Utilities;

public class Soldier extends Unit
{
    public MapLocation target;

    public Soldier()
    {

    }

    public Soldier(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfSoldiersOdd.ordinal(), Messaging.NumbOfSoldiersEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }
        else if (nearByEnemies.length > 0)
        {
            return false;
        }
        return nav.takeNextStep(target, true, false);
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
            return new SoldierRusher(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
