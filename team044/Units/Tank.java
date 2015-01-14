package team044.Units;

import team044.*;

import battlecode.common.*;
import team044.Units.Rushers.TankRusher;

public class Tank extends Unit
{
    public Tank()
    {
        // Houston we have a problem
    }

    public Tank(RobotController rc)
    {
        super(rc);

        nav.setAvoidTowers(false);
        nav.setAvoidHQ(false);

        rc.setIndicatorString(0, "Standard Tank");
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        // TODO: Add code to smartly move forward so the entire army moves together
        //target = Utilities.getRushLocation(rc);
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
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfTanksOdd.ordinal(), Messaging.NumbOfTanksEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
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
