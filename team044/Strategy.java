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

            rc.setIndicatorString(0, "Small map, enemy unit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);
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
            rc.setIndicatorString(0, "Large Map, mostUnit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);

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

    public static int loneTowers(RobotController rc) throws GameActionException {

        MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
        MapLocation[] myTowers = rc.senseTowerLocations();
        if (enemyTowers.length == 0)
        {
            return 0;
        }
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        int numMine = myTowers.length;
        int numbTowers = enemyTowers.length;
        int[][] towers = new int[enemyTowers.length][4];
        // Determine the mean, standard deviation, and range of enemy tower locations.
        int meanX = 0;
        int meanY = 0;
        int myMeanX = 0;
        int myMeanY = 0;
        for (int i = 0; i < numbTowers; i++) {
            towers[i][0] = enemyTowers[i].x;
            towers[i][1] = enemyTowers[i].y;
            meanX += enemyTowers[i].x;
            meanY += enemyTowers[i].y;
            myMeanX += myTowers[i].x;
            myMeanY += myTowers[i].y;
        }
        meanX = meanX / numbTowers;
        meanY = meanY / numbTowers;
        myMeanX = myMeanX / numMine;
        myMeanY = myMeanY / numMine;
        MapLocation center = new MapLocation(meanX, meanY);
        MapLocation myCenter = new MapLocation(myMeanX,myMeanY);
        MapLocation[] far = new MapLocation[4];
        far[0] = enemyTowers[0];
        far[1] = enemyTowers[0];
        far[2] = enemyTowers[0];
        far[3] = enemyTowers[0];
        for (int i = 0; i < numbTowers; i++) {
            towers[i][2] = enemyTowers[i].distanceSquaredTo(enemyHQ);
            towers[i][3] = 99999999;
            for (int j = 0; j < numbTowers; j++) {
                if (j != i) {
                    int d = enemyTowers[i].distanceSquaredTo(enemyTowers[j]);
                    if (d < towers[i][3])
                        towers[i][3] = d;
                }
            }
            if (far[3].x > towers[i][0])
                far[3] = enemyTowers[i];
            if (far[1].x < towers[i][0])
                far[1] = enemyTowers[i];
            if (far[0].y > towers[i][1])
                far[0] = enemyTowers[i];
            if (far[2].y < towers[i][1])
                far[2] = enemyTowers[i];
        }
        for (int i = 0; i < enemyTowers.length; i++) {
            if (far[3].x == towers[i][0])
                System.out.println("Far West: " + far[3].x + "," + far[3].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
            if (far[1].x == towers[i][0])
                System.out.println("Far East: " + far[1].x + "," + far[1].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
            if (far[0].y == towers[i][1])
                System.out.println("Far North: " + far[0].x + "," + far[0].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
            if (far[2].y == towers[i][1])
                System.out.println("Far South: " + far[2].x + "," + far[2].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
        }
        MapLocation ourHQ = rc.senseHQLocation();
        Direction toCenter = ourHQ.directionTo(enemyHQ);
        Direction[] extremes = new Direction[4];
        extremes[0] = ourHQ.directionTo(far[0]);
        extremes[1] = ourHQ.directionTo(far[1]);
        extremes[2] = ourHQ.directionTo(far[2]);
        extremes[3] = ourHQ.directionTo(far[3]);
        int group2 = 0;
        int group3 = 0;

        for (int i = 0; i < 4; i++)
        {
            int degrees = 0;
            while (!extremes[i].equals(toCenter))
            {
                extremes[i] = extremes[i].rotateLeft();
                degrees++;
            }
            switch(degrees)
            {
                case 0:
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    if (far[i].distanceSquaredTo(center) > 300) {
                        group2 = 1;
                        System.out.println(far[i].x + ","+far[i].y);
                    }
                    break;
                case 5:
                case 6:
                case 7:
                    if (far[i].distanceSquaredTo(center) > 300)
                        group3 = 2;
                    break;
                default:
                    break;
            }
        }
        return group2+group3;
    }
}
