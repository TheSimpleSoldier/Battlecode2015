package _teamSprintBot;

import battlecode.common.*;

/**
 * Created by David on 1/11/2015.
 */
public class Strategy
{
    public static BuildOrderMessaging[] strat;

    // Determine strategy and broadcast info to appropriate channels
    public static BuildOrderMessaging[] initialStrategy(RobotController rc) throws GameActionException
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
        BuildOrderMessaging primaryStructure;
        BuildOrderMessaging secondaryStructure;
        BuildOrderMessaging tertiaryStructure;
        BuildOrderMessaging miningType;
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

        String debug = String.format("HP: %d; Size: %d; First Attacker: %d; Attack Timing: %d; Unit #1: %d; ByteCodes left: %d", endGameHP, hqDistance, mostEndGameUnit, attackTiming, mostEndGameUnit, Clock.getBytecodesLeft());

        System.out.println(debug);
        // Small map
        if (hqDistance < 2500) {
            // Decide unit build based on previous game's unit build and map size.
            switch ((int) mostEndGameUnit) {
                case 1:     // First attack on tower/HQ was with a drone last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildHelipad;
                    break;
                case 2:     // First attack on tower/HQ was with a Launcher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    break;
                case 3:     // First attack on tower/HQ was with a Tank last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    break;
                default:
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    break;
            }

            // for short maps build 3 of main unit combo
            strat = new BuildOrderMessaging[35];
            strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[1] = BuildOrderMessaging.BuildMinerFactory;
            strat[2] = primaryStructure;
            strat[3] = secondaryStructure;
            strat[4] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[5] = tertiaryStructure;
            strat[6] = tertiaryStructure;
            strat[7] = tertiaryStructure;
            strat[8] = tertiaryStructure;
            strat[9] = BuildOrderMessaging.BuildSupplyDepot;
            strat[10] = BuildOrderMessaging.BuildSupplyDepot;
            strat[11] = BuildOrderMessaging.BuildSupplyDepot;
            strat[12] = BuildOrderMessaging.BuildSupplyDepot;
            strat[13] = BuildOrderMessaging.BuildSupplyDepot;
            strat[14] = BuildOrderMessaging.BuildSupplyDepot;
            strat[15] = BuildOrderMessaging.BuildSupplyDepot;
            strat[16] = BuildOrderMessaging.BuildSupplyDepot;
            strat[17] = BuildOrderMessaging.BuildSupplyDepot;
            strat[18] = BuildOrderMessaging.BuildSupplyDepot;
            strat[19] = BuildOrderMessaging.BuildSupplyDepot;
            strat[20] = BuildOrderMessaging.BuildSupplyDepot;
            strat[21] = BuildOrderMessaging.BuildSupplyDepot;
            strat[22] = BuildOrderMessaging.BuildSupplyDepot;
            strat[23] = BuildOrderMessaging.BuildSupplyDepot;
            strat[24] = tertiaryStructure;
            strat[25] = BuildOrderMessaging.BuildSupplyDepot;
            strat[26] = BuildOrderMessaging.BuildSupplyDepot;
            strat[27] = BuildOrderMessaging.BuildSupplyDepot;
            strat[28] = BuildOrderMessaging.BuildSupplyDepot;
            strat[29] = BuildOrderMessaging.BuildSupplyDepot;
            strat[30] = BuildOrderMessaging.BuildSupplyDepot;
            strat[31] = BuildOrderMessaging.BuildSupplyDepot;
            strat[32] = BuildOrderMessaging.BuildSupplyDepot;
            strat[33] = tertiaryStructure;
            strat[34] = BuildOrderMessaging.BuildSupplyDepot;

            rc.setIndicatorString(0, "Small map, enemy unit: " + mostEndGameUnit + ", dist: " + hqDistance);
        }
        // Large map
        else if (hqDistance > 5000)
        {
            // Decide unit build based on previous game's unit build and map size.
            switch ((int) mostEndGameUnit)
            {
                case 1:     // First attack on tower/HQ was with a drone last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildHelipad;
                    miningType = BuildOrderMessaging.BuildMiningBaracks;
                    break;
                case 2:     // First attack on tower/HQ was with a Launcher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    miningType = BuildOrderMessaging.BuildMiningAeroSpaceLab;
                    break;
                case 3:     // First attack on tower/HQ was with a Tank last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    miningType = BuildOrderMessaging.BuildMinerFactory;
                    break;
                default:
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    miningType = BuildOrderMessaging.BuildMiningAeroSpaceLab;
                    break;
            }
            strat = new BuildOrderMessaging[39];
            strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[1] = BuildOrderMessaging.BuildMinerFactory;
            strat[2] = miningType;
            strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[4] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[5] = primaryStructure;
            strat[6] = secondaryStructure;
            strat[7] = tertiaryStructure;
            strat[8] = tertiaryStructure;
            strat[9] = tertiaryStructure;
            strat[10] = tertiaryStructure;
            strat[11] = BuildOrderMessaging.BuildSupplyDepot;
            strat[12] = miningType;
            strat[13] = BuildOrderMessaging.BuildSupplyDepot;
            strat[14] = tertiaryStructure;
            strat[15] = BuildOrderMessaging.BuildSupplyDepot;
            strat[16] = BuildOrderMessaging.BuildSupplyDepot;
            strat[17] = BuildOrderMessaging.BuildSupplyDepot;
            strat[18] = BuildOrderMessaging.BuildSupplyDepot;
            strat[19] = BuildOrderMessaging.BuildSupplyDepot;
            strat[20] = tertiaryStructure;
            strat[21] = BuildOrderMessaging.BuildSupplyDepot;
            strat[22] = BuildOrderMessaging.BuildSupplyDepot;
            strat[23] = BuildOrderMessaging.BuildSupplyDepot;
            strat[24] = BuildOrderMessaging.BuildSupplyDepot;
            strat[25] = BuildOrderMessaging.BuildSupplyDepot;
            strat[26] = BuildOrderMessaging.BuildSupplyDepot;
            strat[27] = tertiaryStructure;
            strat[28] = BuildOrderMessaging.BuildSupplyDepot;
            strat[29] = BuildOrderMessaging.BuildSupplyDepot;
            strat[30] = BuildOrderMessaging.BuildSupplyDepot;
            strat[31] = BuildOrderMessaging.BuildSupplyDepot;
            strat[32] = BuildOrderMessaging.BuildSupplyDepot;
            strat[33] = BuildOrderMessaging.BuildSupplyDepot;
            strat[34] = BuildOrderMessaging.BuildSupplyDepot;
            strat[35] = tertiaryStructure;
            strat[36] = BuildOrderMessaging.BuildSupplyDepot;
            strat[37] = BuildOrderMessaging.BuildSupplyDepot;
            strat[38] = BuildOrderMessaging.BuildSupplyDepot;
            rc.setIndicatorString(0, "Large Map, mostUnit: " + mostEndGameUnit + ", dist: " + hqDistance);

        }
        // Default Strategy
        else
        {
            // Decide unit build based on previous game's unit build and map size.
            switch ((int) mostEndGameUnit)
            {
                case 1:     // First attack on tower/HQ was with a drone last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildHelipad;
                    miningType = BuildOrderMessaging.BuildMiningBaracks;
                    break;
                case 2:     // First attack on tower/HQ was with a Launcher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    miningType = BuildOrderMessaging.BuildAerospaceLab;
                    break;
                case 3:     // First attack on tower/HQ was with a Tank last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    miningType = BuildOrderMessaging.BuildMinerFactory;
                    break;
                default:
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    miningType = BuildOrderMessaging.BuildMiningAeroSpaceLab;
                    break;
            }
            strat = new BuildOrderMessaging[39];
            strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[1] = BuildOrderMessaging.BuildMinerFactory;
            strat[2] = primaryStructure;
            strat[3] = secondaryStructure;
            strat[4] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[5] = miningType;
            strat[6] = tertiaryStructure;
            strat[7] = tertiaryStructure;
            strat[8] = tertiaryStructure;
            strat[9] = tertiaryStructure;
            strat[10] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[11] = tertiaryStructure;
            strat[12] = BuildOrderMessaging.BuildSupplyDepot;
            strat[13] = BuildOrderMessaging.BuildSupplyDepot;
            strat[14] = BuildOrderMessaging.BuildSupplyDepot;
            strat[15] = tertiaryStructure;
            strat[16] = BuildOrderMessaging.BuildSupplyDepot;
            strat[17] = BuildOrderMessaging.BuildSupplyDepot;
            strat[18] = BuildOrderMessaging.BuildSupplyDepot;
            strat[19] = BuildOrderMessaging.BuildSupplyDepot;
            strat[20] = tertiaryStructure;
            strat[21] = BuildOrderMessaging.BuildSupplyDepot;
            strat[22] = BuildOrderMessaging.BuildSupplyDepot;
            strat[23] = BuildOrderMessaging.BuildSupplyDepot;
            strat[24] = BuildOrderMessaging.BuildSupplyDepot;
            strat[25] = BuildOrderMessaging.BuildSupplyDepot;
            strat[26] = BuildOrderMessaging.BuildSupplyDepot;
            strat[27] = BuildOrderMessaging.BuildSupplyDepot;
            strat[28] = tertiaryStructure;
            strat[29] = BuildOrderMessaging.BuildSupplyDepot;
            strat[30] = BuildOrderMessaging.BuildSupplyDepot;
            strat[31] = BuildOrderMessaging.BuildSupplyDepot;
            strat[32] = BuildOrderMessaging.BuildSupplyDepot;
            strat[33] = BuildOrderMessaging.BuildSupplyDepot;
            strat[34] = BuildOrderMessaging.BuildSupplyDepot;
            strat[35] = BuildOrderMessaging.BuildSupplyDepot;
            strat[36] = BuildOrderMessaging.BuildSupplyDepot;
            strat[37] = BuildOrderMessaging.BuildSupplyDepot;
            strat[38] = BuildOrderMessaging.BuildSupplyDepot;
            rc.setIndicatorString(0, "Default " + debug + ", " + mostEndGameUnit + ", dist: " + hqDistance);
        }
        return strat;
    }
}
