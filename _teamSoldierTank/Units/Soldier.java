package _teamSoldierTank.Units;

import _teamSoldierTank.BuildOrderMessaging;
import _teamSoldierTank.Messaging;
import _teamSoldierTank.Unit;
import battlecode.common.*;
import _teamSoldierTank.Units.Defenders.DefensiveSoldiers;
import _teamSoldierTank.Units.Followers.SoldierFollower;
import _teamSoldierTank.Units.Rushers.SoldierRusher;
import _teamSoldierTank.Units.SquadUnits.SoldierSquad;
import _teamSoldierTank.Units.SupportingUnits.SupportingSoldier;
import _teamSoldierTank.Units.harrassers.SoldierHarrasser;
import _teamSoldierTank.Utilities;

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
        rc.setIndicatorString(2, "Type: " + type);
        rc.broadcast(Messaging.SoldierType.ordinal(), -1);

        if (type == BuildOrderMessaging.BuildHarrassSoldier.ordinal())
        {
            return new SoldierHarrasser(rc);
        }
        else if (type == BuildOrderMessaging.BuildDefensiveSoldier.ordinal())
        {
            return new DefensiveSoldiers(rc);
        }
        else if (type == BuildOrderMessaging.BuildSquadSoldier.ordinal())
        {
            return new SoldierSquad(rc);
        }
        else if (type == BuildOrderMessaging.BuildSupportingSoldier.ordinal())
        {
            return new SupportingSoldier(rc);
        }
        else if (type == BuildOrderMessaging.BuildFollowerSoldier.ordinal())
        {
            return new SoldierFollower(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
