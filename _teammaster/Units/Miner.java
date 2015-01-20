package _teammaster.Units;


import _teammaster.Messaging;
import _teammaster.Unit;
import _teammaster.Utilities;

import battlecode.common.*;
import _teammaster.Units.Rushers.MinerRusher;

public class Miner extends Unit
{
    boolean mineToOurHQ = true;

    public Miner(RobotController rc)
    {
        super(rc);

        rc.setIndicatorString(0, "Miner to our HQ");
        if (rc.getID() % 2 == 0)
        {
            rc.setIndicatorString(0, "Miner to enemyHQ");
            mineToOurHQ = false;
        }
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();

        if (rc.senseOre(rc.getLocation()) < 2)
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
        if (rc.isCoreReady() && rc.canMine() && rc.senseOre(rc.getLocation()) >= 2)
        {
            rc.mine();
            return true;
        }

        return false;
    }
}
