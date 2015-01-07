package theSimpleSoldier;

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
    int numbOfSpawnedSoldiers = 0;
    Direction[] dirs;
    FightMicro fighter;

    BuildOrderMessaging[] strat;
    int currentUnit = 0;

    public HQ(RobotController rc) throws GameActionException
    {
        this.rc = rc;
        range = rc.getType().attackRadiusSquared;
        us = rc.getTeam();
        opponent = us.opponent();
        fighter = new FightMicro(rc);
        strat = new BuildOrderMessaging[100];
        strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[1] = BuildOrderMessaging.BuildMinerFactory;
        strat[2] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[3] = BuildOrderMessaging.BuildMinerFactory;
        strat[4] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[5] = BuildOrderMessaging.BuildMinerFactory;
        strat[6] = BuildOrderMessaging.BuildBeaverBuilder;
        strat[7] = BuildOrderMessaging.BuildHelipad;
        strat[8] = BuildOrderMessaging.BuildBaracks;
        strat[9] = BuildOrderMessaging.BuildAerospaceLab;
        strat[10] = BuildOrderMessaging.BuildAerospaceLab;
        strat[11] = BuildOrderMessaging.BuildTankFactory;
    }

    public void handleMessages() throws GameActionException
    {
        rc.broadcast(Messaging.BuildOrder.ordinal(), BuildOrderMessaging.BuildBaracks.ordinal());

        if (currentUnit >= strat.length)
        {
            // we are done excecuting build order
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
            }

            // state which building we want built next
            rc.broadcast(Messaging.BuildOrder.ordinal(), strat[currentUnit].ordinal());
        }

        rc.broadcast(Messaging.NumbOfBeavers.ordinal(), numbOfSpawnedSoldiers);


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
                numbOfSpawnedSoldiers++;
                currentUnit++;
                return true;
            }
        }

        return false;
    }
}
