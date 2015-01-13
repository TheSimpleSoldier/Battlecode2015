package team044;

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
        long mostEndGameUnit = memory[TeamMemory.EnemyUnitBuild.ordinal()] >>> 4;
        long secondMostEndGame = memory[TeamMemory.EnemyUnitBuild.ordinal()] & 15;
        long endGameHP = memory[TeamMemory.HQHP.ordinal()];
        BuildOrderMessaging primaryStructure;
        BuildOrderMessaging secondaryStructure;
        BuildOrderMessaging tertiaryStructure;
        Direction toEnemy = rc.getLocation().directionTo(enemyHQ);
        MapLocation mapEdge = enemyHQ.add(toEnemy);
        int count = 0;

        while (rc.isPathable(RobotType.MINER, mapEdge)) {
            count++;
            mapEdge = mapEdge.add(toEnemy);
        }

        hqDistance = (int) Math.sqrt((double) hqDistance);
        hqDistance += count + count;

        String debug = String.format("HP: %d; Size: %d; First Attacker: %d; Attack Timing: %d; Unit #1: %d; #2: %d; ByteCodes left: %d", endGameHP, hqDistance, mostInitialAttackers, attackTiming, mostEndGameUnit, secondMostEndGame, Clock.getBytecodesLeft());

        System.out.println(debug);
        // Small map
        if (hqDistance < 2000) {
            // Decide unit build based on previous game's unit build and map size.
            switch ((int) mostInitialAttackers) {
                case 1:     // First attack on tower/HQ was with a drone last game.
                    primaryStructure = BuildOrderMessaging.BuildTankFactory;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 2:     // First attack on tower/HQ was with a Launcher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 3:     // First attack on tower/HQ was with a Tank last game.
                    primaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildHelipad;
                    break;
                case 4:     // First attack on tower/HQ was with a Basher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 5:     // First attack on tower/HQ was with a Soldier last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                default:
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildTankFactory;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
            }
            // Enemy rushed last game
            if (attackTiming < 700) {
                strat = new BuildOrderMessaging[23];
                strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[1] = BuildOrderMessaging.BuildMinerFactory;
                strat[2] = BuildOrderMessaging.BuildBeaverMiner;
                strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[4] = primaryStructure;
                strat[5] = BuildOrderMessaging.BuildBeaverMiner;
                strat[6] = secondaryStructure;
                strat[7] = BuildOrderMessaging.BuildMinerFactory;
                strat[8] = BuildOrderMessaging.BuildBaracks;
                strat[9] = BuildOrderMessaging.BuildHelipad;
                strat[10] = primaryStructure;
                strat[11] = primaryStructure;
                strat[12] = BuildOrderMessaging.BuildSupplyDepot;
                strat[13] = primaryStructure;
                strat[14] = BuildOrderMessaging.BuildSupplyDepot;
                strat[15] = primaryStructure;
                strat[16] = secondaryStructure;
                strat[17] = BuildOrderMessaging.BuildSupplyDepot;
                strat[18] = BuildOrderMessaging.BuildSupplyDepot;
                strat[19] = BuildOrderMessaging.BuildSupplyDepot;
                strat[20] = BuildOrderMessaging.BuildSupplyDepot;
                strat[21] = BuildOrderMessaging.BuildSupplyDepot;
                strat[22] = BuildOrderMessaging.BuildSupplyDepot;
                rc.setIndicatorString(0, "Small map + rush " + debug + " ");
                return strat;
            }
            // Fewer than 6 towers
            else if (numbTowers < 6)
            {
                strat = new BuildOrderMessaging[30];
                strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[1] = BuildOrderMessaging.BuildMinerFactory;
                strat[2] = BuildOrderMessaging.BuildBeaverMiner;
                strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[4] = BuildOrderMessaging.BuildMinerFactory;
                strat[5] = BuildOrderMessaging.BuildBeaverMiner;
                strat[6] = primaryStructure;
                strat[7] = BuildOrderMessaging.BuildBaracks;
                strat[8] = BuildOrderMessaging.BuildHelipad;
                strat[9] = primaryStructure;
                strat[10] = primaryStructure;
                strat[11] = primaryStructure;
                strat[12] = BuildOrderMessaging.BuildSupplyDepot;
                strat[13] = secondaryStructure;
                strat[14] = BuildOrderMessaging.BuildSupplyDepot;
                strat[15] = primaryStructure;
                strat[16] = BuildOrderMessaging.BuildSupplyDepot;
                strat[17] = BuildOrderMessaging.BuildSupplyDepot;
                strat[18] = BuildOrderMessaging.BuildSupplyDepot;
                strat[19] = BuildOrderMessaging.BuildSupplyDepot;
                strat[20] = BuildOrderMessaging.BuildSupplyDepot;
                strat[21] = BuildOrderMessaging.BuildSupplyDepot;
                strat[22] = BuildOrderMessaging.BuildSupplyDepot;
                rc.setIndicatorString(0, "Small map + few towers " + debug + " ");
                return strat;
            }
            // Just a small map
            else
            {
                strat = new BuildOrderMessaging[30];
                strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[1] = BuildOrderMessaging.BuildMinerFactory;
                strat[2] = BuildOrderMessaging.BuildBeaverMiner;
                strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[4] = BuildOrderMessaging.BuildMinerFactory;
                strat[5] = BuildOrderMessaging.BuildBeaverMiner;
                strat[6] = primaryStructure;
                strat[7] = BuildOrderMessaging.BuildBaracks;
                strat[8] = BuildOrderMessaging.BuildHelipad;
                strat[9] = primaryStructure;
                strat[10] = primaryStructure;
                strat[11] = primaryStructure;
                strat[12] = BuildOrderMessaging.BuildSupplyDepot;
                strat[13] = primaryStructure;
                strat[14] = BuildOrderMessaging.BuildSupplyDepot;
                strat[15] = primaryStructure;
                strat[16] = BuildOrderMessaging.BuildSupplyDepot;
                strat[17] = BuildOrderMessaging.BuildSupplyDepot;
                strat[18] = BuildOrderMessaging.BuildSupplyDepot;
                strat[19] = BuildOrderMessaging.BuildSupplyDepot;
                strat[20] = BuildOrderMessaging.BuildSupplyDepot;
                strat[21] = BuildOrderMessaging.BuildSupplyDepot;
                strat[22] = BuildOrderMessaging.BuildSupplyDepot;
                rc.setIndicatorString(0, "Small map " + debug + " ");
                return strat;
            }
        }
        // Large map
        else if (hqDistance > 5000)
        {
            // Decide unit build based on previous game's unit build and map size.
            switch ((int) mostInitialAttackers)
            {
                case 1:     // First attack on tower/HQ was with a drone last game.
                    primaryStructure = BuildOrderMessaging.BuildTankFactory;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 2:     // First attack on tower/HQ was with a Launcher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 3:     // First attack on tower/HQ was with a Tank last game.
                    primaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildHelipad;
                    break;
                case 4:     // First attack on tower/HQ was with a Basher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 5:     // First attack on tower/HQ was with a Soldier last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                default:
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildTankFactory;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
            }
            // Enemy rushed last game
            if (attackTiming < 600)
            {
                strat = new BuildOrderMessaging[30];
                strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[1] = BuildOrderMessaging.BuildMinerFactory;
                strat[2] = BuildOrderMessaging.BuildBeaverMiner;
                strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[4] = BuildOrderMessaging.BuildMinerFactory;
                strat[5] = BuildOrderMessaging.BuildBeaverMiner;
                strat[6] = primaryStructure;
                strat[7] = BuildOrderMessaging.BuildBaracks;
                strat[8] = BuildOrderMessaging.BuildHelipad;
                strat[9] = primaryStructure;
                strat[10] = primaryStructure;
                strat[11] = primaryStructure;
                strat[12] = BuildOrderMessaging.BuildSupplyDepot;
                strat[13] = primaryStructure;
                strat[14] = BuildOrderMessaging.BuildSupplyDepot;
                strat[15] = primaryStructure;
                strat[16] = BuildOrderMessaging.BuildSupplyDepot;
                strat[17] = BuildOrderMessaging.BuildSupplyDepot;
                strat[18] = BuildOrderMessaging.BuildSupplyDepot;
                strat[19] = BuildOrderMessaging.BuildSupplyDepot;
                strat[20] = BuildOrderMessaging.BuildSupplyDepot;
                strat[21] = BuildOrderMessaging.BuildSupplyDepot;
                strat[22] = BuildOrderMessaging.BuildSupplyDepot;
                rc.setIndicatorString(0, "Large map + rush " + debug + " ");
                return strat;
            }
            // Fewer than 6 towers
            else if (numbTowers < 6)
            {
                strat = new BuildOrderMessaging[30];
                strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[1] = BuildOrderMessaging.BuildMinerFactory;
                strat[2] = BuildOrderMessaging.BuildBeaverMiner;
                strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[4] = BuildOrderMessaging.BuildMinerFactory;
                strat[5] = BuildOrderMessaging.BuildBeaverMiner;
                strat[6] = primaryStructure;
                strat[7] = BuildOrderMessaging.BuildBaracks;
                strat[8] = BuildOrderMessaging.BuildHelipad;
                strat[9] = primaryStructure;
                strat[10] = primaryStructure;
                strat[11] = primaryStructure;
                strat[12] = BuildOrderMessaging.BuildSupplyDepot;
                strat[13] = primaryStructure;
                strat[14] = BuildOrderMessaging.BuildSupplyDepot;
                strat[15] = primaryStructure;
                strat[16] = BuildOrderMessaging.BuildSupplyDepot;
                strat[17] = secondaryStructure;
                strat[18] = BuildOrderMessaging.BuildSupplyDepot;
                strat[19] = secondaryStructure;
                strat[20] = BuildOrderMessaging.BuildSupplyDepot;
                strat[21] = BuildOrderMessaging.BuildSupplyDepot;
                strat[22] = BuildOrderMessaging.BuildSupplyDepot;
                rc.setIndicatorString(0, "Large map + few towers " + debug + " ");
                return strat;
            }
            // Just a large map
            else
            {
                strat = new BuildOrderMessaging[30];
                strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[1] = BuildOrderMessaging.BuildMinerFactory;
                strat[2] = BuildOrderMessaging.BuildBeaverMiner;
                strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
                strat[4] = BuildOrderMessaging.BuildMinerFactory;
                strat[5] = BuildOrderMessaging.BuildBeaverMiner;
                strat[6] = primaryStructure;
                strat[7] = BuildOrderMessaging.BuildBaracks;
                strat[8] = BuildOrderMessaging.BuildHelipad;
                strat[9] = primaryStructure;
                strat[10] = primaryStructure;
                strat[11] = primaryStructure;
                strat[12] = BuildOrderMessaging.BuildSupplyDepot;
                strat[13] = primaryStructure;
                strat[14] = BuildOrderMessaging.BuildSupplyDepot;
                strat[15] = primaryStructure;
                strat[16] = BuildOrderMessaging.BuildSupplyDepot;
                strat[17] = BuildOrderMessaging.BuildSupplyDepot;
                strat[18] = BuildOrderMessaging.BuildSupplyDepot;
                strat[19] = BuildOrderMessaging.BuildSupplyDepot;
                strat[20] = BuildOrderMessaging.BuildSupplyDepot;
                strat[21] = BuildOrderMessaging.BuildSupplyDepot;
                strat[22] = BuildOrderMessaging.BuildSupplyDepot;
                rc.setIndicatorString(0, "Large map " + debug + " ");
                return strat;
            }

        }
        // Default Strategy
        else
        {
            // Decide unit build based on previous game's unit build and map size.
            switch ((int) mostInitialAttackers)
            {
                case 1:     // First attack on tower/HQ was with a drone last game.
                    primaryStructure = BuildOrderMessaging.BuildTankFactory;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 2:     // First attack on tower/HQ was with a Launcher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 3:     // First attack on tower/HQ was with a Tank last game.
                    primaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
                    tertiaryStructure = BuildOrderMessaging.BuildHelipad;
                    break;
                case 4:     // First attack on tower/HQ was with a Basher last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                case 5:     // First attack on tower/HQ was with a Soldier last game.
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildHelipad;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
                default:
                    primaryStructure = BuildOrderMessaging.BuildHelipad;
                    secondaryStructure = BuildOrderMessaging.BuildTankFactory;
                    tertiaryStructure = BuildOrderMessaging.BuildTankFactory;
                    break;
            }
            strat = new BuildOrderMessaging[30];
            strat[0] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[1] = BuildOrderMessaging.BuildMinerFactory;
            strat[2] = BuildOrderMessaging.BuildBeaverMiner;
            strat[3] = BuildOrderMessaging.BuildBeaverBuilder;
            strat[4] = BuildOrderMessaging.BuildMinerFactory;
            strat[5] = BuildOrderMessaging.BuildBeaverMiner;
            strat[6] = primaryStructure;
            strat[7] = BuildOrderMessaging.BuildBaracks;
            strat[8] = BuildOrderMessaging.BuildHelipad;
            strat[9] = primaryStructure;
            strat[10] = primaryStructure;
            strat[11] = secondaryStructure;
            strat[12] = BuildOrderMessaging.BuildSupplyDepot;
            strat[13] = primaryStructure;
            strat[14] = BuildOrderMessaging.BuildSupplyDepot;
            strat[15] = secondaryStructure;
            strat[16] = BuildOrderMessaging.BuildSupplyDepot;
            strat[17] = BuildOrderMessaging.BuildSupplyDepot;
            strat[18] = BuildOrderMessaging.BuildSupplyDepot;
            strat[19] = BuildOrderMessaging.BuildSupplyDepot;
            strat[20] = BuildOrderMessaging.BuildSupplyDepot;
            strat[21] = BuildOrderMessaging.BuildSupplyDepot;
            strat[22] = BuildOrderMessaging.BuildSupplyDepot;
            rc.setIndicatorString(0, "Default " + debug + " ");
            return strat;
        }
    }
}
