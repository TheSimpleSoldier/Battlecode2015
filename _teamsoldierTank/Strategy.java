package _teamsoldierTank;

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
        BuildOrderMessaging flankingStructure;
        BuildOrderMessaging secondBeaver;
        BuildOrderMessaging thirdBeaver;
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

        if (enemiesSeen > 1000)
        {
            defensiveStructure = BuildOrderMessaging.BuildTankFactory;
            BuildOrderMessaging[] tankStrat = {BuildOrderMessaging.BuildDefensiveTank, BuildOrderMessaging.BuildSquadTank};
            messenger.changeTankStrat(tankStrat);
        }
        else
        {
            defensiveStructure = null;
        }

        System.out.println(debug);
        // Very Small map
        if (hqDistance < 1000)
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildBaracks;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            flankingStructure = null;
            miningType = null;
            miningType2 = null;
            secondBeaver = null;
            thirdBeaver = BuildOrderMessaging.BuildBeaverBuilder;

            //messenger.setGroup1(20,0,10,0,true);
            //messenger.setGroup2(0,0,0,0,false);
            //messenger.setGroup3(0, 0, 0, 0, false);

            rc.setIndicatorString(0, "Very Small map, enemy unit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);
        }
        // Small map
        else if (hqDistance < 2500)
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildBaracks;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            flankingStructure = BuildOrderMessaging.BuildTankFactory;
            miningType = null;
            miningType2 = null;
            secondBeaver = null;//BuildOrderMessaging.BuildBeaverBuilder;
            thirdBeaver = BuildOrderMessaging.BuildBeaverBuilder;

            rc.setIndicatorString(0, "Small map, enemy unit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);
        }
        // Large map
        else if (hqDistance > 5000)
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            flankingStructure = BuildOrderMessaging.BuildTankFactory;
            miningType = BuildOrderMessaging.BuildMiningBaracks;
            miningType2 = BuildOrderMessaging.BuildMiningBaracks;
            secondBeaver = BuildOrderMessaging.BuildBeaverBuilder;
            thirdBeaver = BuildOrderMessaging.BuildBeaverBuilder;

            rc.setIndicatorString(0, "Large Map, mostUnit: " + mostEndGameUnit + ", dist: " + hqDistance + ", " + debug);

        }
        // Default Strategy
        else
        {
            primaryStructure = BuildOrderMessaging.BuildHelipad;
            secondaryStructure = BuildOrderMessaging.BuildBaracks;
            tertiaryStructure = BuildOrderMessaging.BuildAerospaceLab;
            flankingStructure = BuildOrderMessaging.BuildTankFactory;
            miningType = BuildOrderMessaging.BuildMiningBaracks;
            miningType2 = null;
            secondBeaver = BuildOrderMessaging.BuildBeaverBuilder;
            thirdBeaver = BuildOrderMessaging.BuildBeaverBuilder;

            rc.setIndicatorString(0, "Default " + debug + ", " + mostEndGameUnit + ", dist: " + hqDistance);
        }


        if (defensiveStructure != null)
        {
            secondaryStructure = null;
            flankingStructure = null;
        }

        if (Strategy.loneTowers(rc) <= 0)
        {
            flankingStructure = null;
        }
        //strat = new BuildOrderMessaging[43];


        BuildOrderMessaging[] strat = {
                BuildOrderMessaging.BuildBeaverBuilder,
                BuildOrderMessaging.BuildMinerFactory,
                BuildOrderMessaging.BuildBeaverBuilder,
                BuildOrderMessaging.BuildBaracks,
                BuildOrderMessaging.BuildTankFactory,
                BuildOrderMessaging.BuildTankFactory,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildBaracks,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildBaracks,
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
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot,
                BuildOrderMessaging.BuildSupplyDepot
        };

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
//        for (int i = 0; i < enemyTowers.length; i++) {
//            if (far[3].x == towers[i][0])
//                System.out.println("Far West: " + far[3].x + "," + far[3].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
//            if (far[1].x == towers[i][0])
//                System.out.println("Far East: " + far[1].x + "," + far[1].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
//            if (far[0].y == towers[i][1])
//                System.out.println("Far North: " + far[0].x + "," + far[0].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
//            if (far[2].y == towers[i][1])
//                System.out.println("Far South: " + far[2].x + "," + far[2].y + "; Distance: " + towers[i][3] + "; HQ Distance: " + towers[i][2]);
//        }
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
//                        System.out.println(far[i].x + ","+far[i].y);
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
