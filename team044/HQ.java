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

    int numbOfBashers   = 0;
    int numbOfBeavers   = 0;
    int numbOfComps     = 0;
    int numbOfDrones    = 0;
    int numbOfLaunchers = 0;
    int numbOfMiners    = 0;
    int numbOfSoldiers  = 0;
    int numbOfTanks     = 0;

    public HQ(RobotController rc) throws GameActionException
    {
        this.rc = rc;
        range = rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
        fighter = new FightMicro(rc);
        messenger = new Messenger(rc);
        strat = new BuildOrderMessaging[23];
        strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[1] = BuildOrderMessaging.BuildMinerFactory;
        strat[2] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[3] = BuildOrderMessaging.BuildMinerFactory;
        strat[4] = BuildOrderMessaging.BuildMinerFactory;
        strat[5] = BuildOrderMessaging.BuildMiningBaracks;
        strat[6] = BuildOrderMessaging.BuildHelipad;
        strat[7] = BuildOrderMessaging.BuildMiningBaracks;
        strat[9] = BuildOrderMessaging.BuildTankFactory;
        strat[10] = BuildOrderMessaging.BuildTankFactory;
        strat[11] = BuildOrderMessaging.BuildTankFactory;
        strat[12] = BuildOrderMessaging.BuildTankFactory;
        strat[13] = BuildOrderMessaging.BuildTankFactory;
        strat[14] = BuildOrderMessaging.BuildHelipad;
        strat[15] = BuildOrderMessaging.BuildTankFactory;
        strat[16] = BuildOrderMessaging.BuildSupplyDepot;
        strat[17] = BuildOrderMessaging.BuildSupplyDepot;
        strat[18] = BuildOrderMessaging.BuildSupplyDepot;
        strat[19] = BuildOrderMessaging.BuildSupplyDepot;
        strat[20] = BuildOrderMessaging.BuildSupplyDepot;
        strat[21] = BuildOrderMessaging.BuildSupplyDepot;
        strat[22] = BuildOrderMessaging.BuildSupplyDepot;

        rc.setIndicatorString(0, "HQ");
    }

    public void handleMessages() throws GameActionException
    {
        rc.setIndicatorString(0, "Messaging");
        messenger.giveUnitOrders();
        rc.setIndicatorString(0, "after give unit orders");

        // at the end of the game rush all units to try and take down the enemy as mining will no longer help us
        if (Clock.getRoundNum() > 1700)
        {
            rc.broadcast(Messaging.RushEnemyBase.ordinal(), 1);
            rc.setIndicatorString(2, "Rushing enemy");
        }

        // even round so odd channel has data
        if (Clock.getRoundNum() % 2 == 0)
        {
            // read in the integer value and then reset channel to 0
            numbOfBashers = rc.readBroadcast(Messaging.NumbOfBashersOdd.ordinal());
            rc.broadcast(Messaging.NumbOfBashersOdd.ordinal(), 0);

            numbOfBeavers = rc.readBroadcast(Messaging.NumbOfBeaverOdd.ordinal());
            rc.broadcast(Messaging.NumbOfBeaverOdd.ordinal(), 0);

            numbOfComps = rc.readBroadcast(Messaging.NumbOfCompsOdd.ordinal());
            rc.broadcast(Messaging.NumbOfCompsOdd.ordinal(), 0);

            numbOfDrones = rc.readBroadcast(Messaging.NumbOfDronesOdd.ordinal());
            rc.broadcast(Messaging.NumbOfDronesOdd.ordinal(), 0);

            numbOfLaunchers = rc.readBroadcast(Messaging.NumbOfLaunchersOdd.ordinal());
            rc.broadcast(Messaging.NumbOfLaunchersOdd.ordinal(), 0);

            numbOfMiners = rc.readBroadcast(Messaging.NumbOfMinersOdd.ordinal());
            rc.broadcast(Messaging.NumbOfMinersOdd.ordinal(),  0);

            numbOfSoldiers = rc.readBroadcast(Messaging.NumbOfSoldiersOdd.ordinal());
            rc.broadcast(Messaging.NumbOfSoldiersOdd.ordinal(), 0);

            numbOfTanks = rc.readBroadcast(Messaging.NumbOfTanksOdd.ordinal());
            rc.broadcast(Messaging.NumbOfTanksOdd.ordinal(), 0);
        }
        // odd round so even channel has complete numb
        else
        {
            // read in the integer value and then reset channel to 0
            numbOfBashers = rc.readBroadcast(Messaging.NumbOfBashersEven.ordinal());
            rc.broadcast(Messaging.NumbOfBashersEven.ordinal(), 0);

            numbOfBeavers = rc.readBroadcast(Messaging.NumbOfBeaverEven.ordinal());
            rc.broadcast(Messaging.NumbOfBeaverEven.ordinal(), 0);

            numbOfComps = rc.readBroadcast(Messaging.NumbOfCompsEven.ordinal());
            rc.broadcast(Messaging.NumbOfCompsEven.ordinal(), 0);

            numbOfDrones = rc.readBroadcast(Messaging.NumbOfDronesEven.ordinal());
            rc.broadcast(Messaging.NumbOfDronesEven.ordinal(), 0);

            numbOfLaunchers = rc.readBroadcast(Messaging.NumbOfLaunchersEven.ordinal());
            rc.broadcast(Messaging.NumbOfLaunchersEven.ordinal(), 0);

            numbOfMiners = rc.readBroadcast(Messaging.NumbOfMinersEven.ordinal());
            rc.broadcast(Messaging.NumbOfMinersEven.ordinal(),  0);

            numbOfSoldiers = rc.readBroadcast(Messaging.NumbOfSoldiersEven.ordinal());
            rc.broadcast(Messaging.NumbOfSoldiersEven.ordinal(), 0);

            numbOfTanks = rc.readBroadcast(Messaging.NumbOfTanksEven.ordinal());
            rc.broadcast(Messaging.NumbOfTanksEven.ordinal(), 0);
        }

        // now broadcast the value for anyone else to use
        rc.broadcast(Messaging.NumbOfBashers.ordinal(), numbOfBashers);
        rc.broadcast(Messaging.NumbOfBeavers.ordinal(), numbOfBeavers);
        rc.broadcast(Messaging.NumbOfComps.ordinal(), numbOfComps);
        rc.broadcast(Messaging.NumbOfDrones.ordinal(), numbOfDrones);
        rc.broadcast(Messaging.NumbOfLaunchers.ordinal(), numbOfLaunchers);
        rc.broadcast(Messaging.NumbOfMiners.ordinal(), numbOfMiners);
        rc.broadcast(Messaging.NumbOfSoldiers.ordinal(), numbOfSoldiers);
        rc.broadcast(Messaging.NumbOfTanks.ordinal(), numbOfTanks);

        rc.setIndicatorString(0, "Bashers: " + numbOfBashers + ", Beavers: " + numbOfBeavers + ", Comps: " + numbOfComps + ", Drones: " + numbOfDrones + ", Launchers: " + numbOfLaunchers + ", Miners: " + numbOfMiners + ", Soldiers: " + numbOfSoldiers + ", Tanks: " + numbOfTanks);
        if (currentUnit < strat.length)
        {
            rc.setIndicatorString(1, ""+strat[currentUnit]);
        }


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

            // something is messed up
            if (strat[currentUnit] == null)
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

                rc.setIndicatorString(1, "" + strat[currentUnit]);
                rc.broadcast(Messaging.BuildOrder.ordinal(), strat[currentUnit].ordinal());
                return true;
            }
        }
        // if we are trying to build a building but don't have any beavers then create a beaver
        else if (numbOfBeavers < 2)
        {
            if (Utilities.spawnUnit(RobotType.BEAVER, rc))
            {
                rc.broadcast(Messaging.BuildOrder.ordinal(), BuildOrderMessaging.BuildBeaverBuilder.ordinal());
                return true;
            }
        }

        return false;
    }
}
