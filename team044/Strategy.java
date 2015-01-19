package team044;

import battlecode.common.*;

public class Strategy
{
    public static BuildOrderMessaging[] strat;

    // Determine strategy and broadcast info to appropriate channels
    public static BuildOrderMessaging[] initialStrategy(RobotController rc, Messenger messenger) throws GameActionException
    {
        MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        int numbTowers = enemyTowers.length;

//        // Determine the mean, standard deviation, and range of enemy tower locations.
//        int meanX = 0;
//        int meanY = 0;
//        for (int i = 0; i < numbTowers; i++)
//        {
//            meanX += enemyTowers[i].x - meanX;
//            meanY += enemyTowers[i].y - meanY;
//        }
//        meanX = meanX/numbTowers;
//        meanY = meanY/numbTowers;
//        double standardDeviationX = 0;
//        double standardDeviationY = 0;
//        for (int i = 0; i < numbTowers; i++)
//        {
//            double enemyX = enemyTowers[i].x - meanX;
//            double enemyY = enemyTowers[i].y - meanY;
//            enemyX *= enemyX;
//            enemyY *= enemyY;
//            standardDeviationX += enemyX;
//            standardDeviationY += enemyY;
//        }
//        standardDeviationX = Math.sqrt(standardDeviationX/numbTowers);
//        standardDeviationY = Math.sqrt(standardDeviationY/numbTowers);
//
//        // Find possible outlier enemy Towers.
//        MapLocation[] outliers = null;
//        for (int i = 0; i < numbTowers; i++)
//        {
//            double enemyX = enemyTowers[i].x - meanX;
//            double enemyY = enemyTowers[i].y - meanY;
//            enemyX /= standardDeviationX;
//            enemyY /= standardDeviationY;
//            if ((standardDeviationX > 1 && enemyX > 2) || (standardDeviationY > 1 && enemyY > 2))
//            {
//                outliers[i] = enemyTowers[i];
//            }
//        }

        int hqDistance = enemyHQ.distanceSquaredTo(rc.getLocation());
        long[] memory = rc.getTeamMemory();     // 32 longs of data from the previous game
        long attackTiming = memory[TeamMemory.AttackTiming.ordinal()] & 4095;
        long mostInitialAttackers = (memory[TeamMemory.AttackTiming.ordinal()] >>> 12) & 15;
        //long secondMost = memory[TeamMemory.AttackTiming.ordinal()] >>> 16;
        long mostEndGameUnit = memory[TeamMemory.EnemyUnitBuild.ordinal()];
        long endGameHP = memory[TeamMemory.HQHP.ordinal()];
        long enemiesSeen = memory[TeamMemory.EnemyHarrass.ordinal()];
        BuildOrderMessaging primaryStructure;
        BuildOrderMessaging secondaryStructure;
        BuildOrderMessaging tertiaryStructure;
        BuildOrderMessaging miningType;
        BuildOrderMessaging miningType2;
        BuildOrderMessaging defensiveStructure;
        Direction toEnemy = rc.getLocation().directionTo(enemyHQ);
        MapLocation mapEdge = enemyHQ.add(toEnemy);
        int count = 0;

        while (rc.isPathable(RobotType.MINER, mapEdge)) {
            count++;
            mapEdge = mapEdge.add(toEnemy);
        }

        hqDistance = (int) Math.sqrt((double) hqDistance);
        hqDistance += count + count;

        hqDistance *= hqDistance;

        String debug = String.format("HP: %d; Size: %d; First Attacker: %d; Attack Timing: %d; Unit #1: %d; ByteCodes left: %d; Enemy Harassers: %d; ", endGameHP, hqDistance, mostEndGameUnit, attackTiming, mostEndGameUnit, Clock.getBytecodesLeft(), enemiesSeen);

        if (enemiesSeen > 5000)
        {
            defensiveStructure = BuildOrderMessaging.BuildTankFactory;
        }
        else
        {
            defensiveStructure = null;
        }

        System.out.println(debug);
        // Small map
        if (hqDistance < 2500)
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildBaracks;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            miningType = null;
            miningType2 = null;

            rc.setIndicatorString(0, "Small map, enemy unit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);
        }
        // Large map
        else if (hqDistance > 5000)
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildBaracks;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;

            miningType = BuildOrderMessaging.BuildMiningBaracks;
            miningType2 = null;

            rc.setIndicatorString(0, "Large Map, mostUnit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);

        }
        // Default Strategy
        else
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;

            miningType = BuildOrderMessaging.BuildMiningBaracks;
            miningType2 = BuildOrderMessaging.BuildMiningBaracks;

            rc.setIndicatorString(0, "Default " + debug + ", " + mostEndGameUnit + ", dist: " + hqDistance);
        }


        if (defensiveStructure != null)
        {
            secondaryStructure = null;
        }
        //strat = new BuildOrderMessaging[43];


        BuildOrderMessaging[] strat = {
                BuildOrderMessaging.BuildBeaverBuilder,
                BuildOrderMessaging.BuildMinerFactory,
                BuildOrderMessaging.BuildBeaverBuilder,
                BuildOrderMessaging.BuildTechnologyInstitute,
                miningType,
                BuildOrderMessaging.BuildTrainingField,
                defensiveStructure,
                primaryStructure,
                tertiaryStructure,
                tertiaryStructure,
                secondaryStructure,
                BuildOrderMessaging.BuildBeaverBuilder,
                miningType2,
                tertiaryStructure,
                tertiaryStructure,
                BuildOrderMessaging.BuildSupplyDepot,
                tertiaryStructure,
                BuildOrderMessaging.BuildBeaverBuilder,
                tertiaryStructure,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                tertiaryStructure,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot
        };

        return strat;
    }
}
