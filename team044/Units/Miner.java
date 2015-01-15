package team044.Units;


import team044.Messaging;
import team044.Unit;
import team044.Utilities;

import battlecode.common.*;
import team044.Units.Rushers.MinerRusher;

public class Miner extends Unit
{
    boolean mineToOurHQ = true;
    MapLocation lastSpot;
    int miningAmount = 5;

    public Miner(RobotController rc)
    {
        super(rc);

        rc.setIndicatorString(0, "Miner to our HQ");
        if (rc.getID() % 2 == 0)
        {
            rc.setIndicatorString(0, "Miner to enemyHQ");
            mineToOurHQ = false;
        }
        lastSpot = rc.getLocation();
        if (rc.senseOre(lastSpot) > 12)
        {
            miningAmount = 12;
        }
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();

        if (lastSpot != rc.getLocation())
        {
            lastSpot = rc.getLocation();
            if (rc.senseOre(lastSpot) > 12)
            {
                miningAmount = 12;
            }
            else if (rc.senseOre(lastSpot) <= 5)
            {
                miningAmount = 2;
            }
            else
            {
                miningAmount = 5;
            }
        }

        if (rc.senseOre(rc.getLocation()) < miningAmount)
        {
            //target = Utilities.getBestMiningSpot(rc);
            target = Utilities.greedyBestMiningSpot(rc);

            if (target == rc.getLocation())
            {
                rc.setIndicatorString(1, "We can't sense a good spot");
                if (mineToOurHQ)
                {
                    target = ourHQ;
                }
                else
                {
                    target = enemyHQ;
                }
            }

            if (rc.getLocation().distanceSquaredTo(ourHQ) < 35)
            {
                mineToOurHQ = false;
            }
        }

        enemies = rc.senseNearbyRobots(24, opponent);

        if (enemies.length > 0)
        {
            rc.broadcast(Messaging.MinerUnderAttackX.ordinal(), enemies[0].location.x);
            rc.broadcast(Messaging.MinerUnderAttackY.ordinal(), enemies[0].location.y);
        }

    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfMinersOdd.ordinal(), Messaging.NumbOfMinersEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            target = enemyHQ;
        }

        return nav.takeNextStep(target);
        //return nav.badMovement(target);
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
            return new MinerRusher(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (rc.isCoreReady() && rc.canMine() && rc.senseOre(rc.getLocation()) >= miningAmount)
        {
            rc.mine();
            return true;
        }

        return false;
    }
}
