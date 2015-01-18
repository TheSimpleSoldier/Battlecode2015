package _teamlaunchers.Units;

import _teamlaunchers.BuildOrderMessaging;
import _teamlaunchers.Messaging;
import _teamlaunchers.Unit;
import battlecode.common.*;
import _teamlaunchers.Units.Defenders.DefensiveSoldiers;
import _teamlaunchers.Units.Rushers.SoldierRusher;
import _teamlaunchers.Units.harrassers.SoldierHarrasser;
import _teamlaunchers.Utilities;

public class Soldier extends Unit
{
    public Soldier()
    {

    }

    public Soldier(RobotController rc)
    {
        super(rc);

        rc.setIndicatorString(0, "Base Soldier");
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
        return nav.takeNextStep(target);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.advancedFightMicro(nearByEnemies);
        //return fighter.basicFightMicro(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        int type = rc.readBroadcast(Messaging.SoldierType.ordinal());
        rc.broadcast(Messaging.SoldierType.ordinal(), -1);

        if (type == BuildOrderMessaging.BuildHarrassSoldier.ordinal())
        {
            return new SoldierHarrasser(rc);
        }
        else if (type == BuildOrderMessaging.BuildDefensiveSoldier.ordinal())
        {
            return new DefensiveSoldiers(rc);
        }

        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
