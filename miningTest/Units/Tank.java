package miningTest.Units;

import miningTest.*;

import battlecode.common.*;
import miningTest.Units.Rushers.TankRusher;

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
        target = Utilities.getRushLocation(rc);
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
