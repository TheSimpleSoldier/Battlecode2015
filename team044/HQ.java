package team044;

import battlecode.common.*;

public class HQ extends Structure
{
    RobotInfo[] enemies;
    RobotInfo[] nearByEnemies;
    RobotInfo[] allies;
    RobotInfo[] nearByAllies;
    int range;
    Team us;
    Team opponent;
    int numberOfMinerFactories = -1;
    Direction[] dirs;
    FightMicro fighter;
    Messenger messenger;
    BuildOrderMessaging[] strat;
    int currentUnit = 0;

    public HQ(RobotController rc) throws GameActionException
    {
        this.rc = rc;
        range = rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
        fighter = new FightMicro(rc);
        messenger = new Messenger(rc);
        strat = new BuildOrderMessaging[15];
        strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[1] = BuildOrderMessaging.BuildHelipad;
        strat[2] = BuildOrderMessaging.BuildHelipad;
        strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[4] = BuildOrderMessaging.BuildMinerFactory;
        strat[5] = BuildOrderMessaging.BuildBeaverMiner;
        strat[6] = BuildOrderMessaging.BuildMinerFactory;
        strat[7] = BuildOrderMessaging.BuildHelipad;
        strat[8] = BuildOrderMessaging.BuildBaracks;
        strat[9] = BuildOrderMessaging.BuildTankFactory;
        strat[10] = BuildOrderMessaging.BuildTankFactory;
        strat[11] = BuildOrderMessaging.BuildTankFactory;
        strat[12] = BuildOrderMessaging.BuildTankFactory;
        strat[13] = BuildOrderMessaging.BuildTankFactory;
        strat[14] = BuildOrderMessaging.BuildTankFactory;
    }

    public void handleMessages() throws GameActionException
    {
        messenger.giveUnitOrders();

        if (currentUnit >= strat.length)
        {
            // we are done excecuting build order
            rc.setIndicatorString(1, "currentUnit >= strat.length ");
        }
        else if (strat[currentUnit] == BuildOrderMessaging.BuildBeaverBuilder)
        {
            rc.broadcast(Messaging.BeaverType.ordinal(), BuildOrderMessaging.BuildBeaverBuilder.ordinal());
        }
        else if (strat[currentUnit] == BuildOrderMessaging.BuildBeaverMiner)
        {
            rc.broadcast(Messaging.BeaverType.ordinal(), BuildOrderMessaging.BuildBeaverMiner.ordinal());
        }
        else
        {
            // if a beaver has taken up a job then we go ahead and post the next building
            if (rc.readBroadcast(Messaging.BuildOrder.ordinal()) == -1)
            {
                System.out.println("Increase Unit count");
                currentUnit++;
                if(currentUnit < strat.length)
                {
                    if(currentUnit >= strat.length && strat[currentUnit] == BuildOrderMessaging.BuildMinerFactory)
                    {
                        numberOfMinerFactories++;
                        rc.broadcast(Messaging.NumbOfBeavers.ordinal(), numberOfMinerFactories);
                    }
                }
            }

            if (currentUnit >= strat.length)
            {
                return;
            }

            // state which building we want built next
            rc.setIndicatorString(1, ""+strat[currentUnit]);
            rc.broadcast(Messaging.BuildOrder.ordinal(), strat[currentUnit].ordinal());
        }

        if (nearByEnemies.length > 0)
        {
            rc.broadcast(Messaging.HQUnderAttack.ordinal(), 1);
        }
        else
        {
            rc.broadcast(Messaging.HQUnderAttack.ordinal(), 0);
        }
    }

    public void collectData() throws GameActionException
    {
        enemies = rc.senseNearbyRobots(99999, opponent);
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
        allies = rc.senseNearbyRobots(99999, us);
        nearByAllies = rc.senseNearbyRobots(range, us);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.structureFightMicro(nearByEnemies);
    }

    public boolean carryOutAbility() throws GameActionException
    {
        if (nearByEnemies.length > 0)
        {
            System.out.println("Near by enemies length to great");
            return false;
        }
        if (currentUnit >= strat.length)
        {
            return false;
        }
        // we only build a beaver if it is the next unit to be built
        if (strat[currentUnit] == BuildOrderMessaging.BuildBeaverBuilder || strat[currentUnit] == BuildOrderMessaging.BuildBeaverMiner)
        {
            if (Utilities.spawnUnit(RobotType.BEAVER, rc))
            {
                currentUnit++;
                if (currentUnit >= strat.length)
                {
                    return true;
                }

                // state which building we want built next
                if (strat[currentUnit] == BuildOrderMessaging.BuildMinerFactory)
                {
                    numberOfMinerFactories++;
                    rc.broadcast(Messaging.NumbOfBeavers.ordinal(), numberOfMinerFactories);
                }

                rc.setIndicatorString(1, ""+strat[currentUnit]);
                rc.broadcast(Messaging.BuildOrder.ordinal(), strat[currentUnit].ordinal());
                return true;
            }
        }

        return false;
    }
}
