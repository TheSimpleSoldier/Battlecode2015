package team044;

import battlecode.common.*;

public class FightMicroUtilities
{

    //===================== Shooting methods ===========================\\

    /**
     * This method returns the RobotInfo for the Robot with the lowest health
     */
    public static RobotInfo findWeakestEnemy(RobotInfo[] nearByEnemies)
    {
        RobotInfo weakest = nearByEnemies[nearByEnemies.length - 1];

        for (int i = nearByEnemies.length-1; --i > 0; )
        {
            if (nearByEnemies[i].health < weakest.health)
            {
                weakest = nearByEnemies[i];
            }
        }

        return weakest;
    }


    /**
     * This method prioritizes towers, then launchers, then the weakest enemy
     */
    public static RobotInfo prioritizeTargets(RobotInfo[] nearByEnemies)
    {
        RobotInfo weakestTower = null;
        RobotInfo weakestLauncher = null;
        RobotInfo weakest = nearByEnemies[0];

        for (int i = 0; i < nearByEnemies.length; i++)
        {
            if (nearByEnemies[i].type == RobotType.TOWER)
            {
                if (weakestTower == null || nearByEnemies[i].health < weakestTower.health)
                {
                    weakestTower = nearByEnemies[i];
                }
            }
            else if (nearByEnemies[i].type == RobotType.LAUNCHER)
            {
                if (weakestLauncher == null || nearByEnemies[i].health < weakestLauncher.health)
                {
                    weakestLauncher = nearByEnemies[i];
                }
            }
            else if (weakest.health > nearByEnemies[i].health)
            {
                weakest = nearByEnemies[i];
            }
        }

        if (weakestTower != null)
        {
            return weakestTower;
        }
        else if (weakestLauncher != null)
        {
            return weakestLauncher;
        }
        return weakest;
    }


    //========================== Methods for standard units like tanks ==========================\\


    /**
     * This method determines if the enemy is more powerful than us
     */
    public static int balanceOfPower(RobotInfo[] enemies, RobotInfo[] allies)
    {
        int alliedHealth = 0;
        int enemyHealth = 0;
        int attack;

        for (int i = allies.length; --i>=0; )
        {
            if (allies[i].type == RobotType.LAUNCHER)
            {
                attack = 60;
            }
            else
            {
                attack = (int) allies[i].type.attackPower;
            }
            alliedHealth += allies[i].health * attack;
        }

        for (int j = enemies.length; --j>=0; )
        {
            if (enemies[j].type == RobotType.LAUNCHER)
            {
                attack = 60;
            }
            else
            {
                attack = (int) enemies[j].type.attackPower;
            }
            enemyHealth += enemies[j].health * attack;
        }

        return alliedHealth - enemyHealth;
    }

    /**
     * This method causes the robot to retreat
     * basically we loop through all of the enemy robots and if we find a direction that we can move
     * in that is opposite of an enemy then we do so
     */
    public static void retreat(RobotController rc, RobotInfo[] enemies) throws GameActionException
    {
        Direction dir;
        MapLocation us = rc.getLocation();

        for (int i = enemies.length; --i>=0; )
        {
            dir = us.directionTo(enemies[i].location).opposite();
            if (rc.canMove(dir))
            {
                rc.move(dir);
                break;
            }
        }

    }

    /**
     * this method advances us towards the enemy
     */
    public static void attack(RobotController rc, RobotInfo[] enemies) throws GameActionException
    {
        Direction dir;
        MapLocation us = rc.getLocation();

        for (int i = enemies.length; --i>=0; )
        {
            dir = us.directionTo(enemies[i].location);
            if (rc.canMove(dir))
            {
                rc.move(dir);
                break;
            }
        }
    }

    /**
     * This method determines if the enemy has launchers
     */
    public static boolean enemyHasLaunchers(RobotInfo[] enemies)
    {
        for (int i = enemies.length; --i>=0; )
        {
            if (enemies[i].type == RobotType.LAUNCHER || enemies[i].type == RobotType.MISSILE)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * This method locks onto a Launcher to kill it
     * or to die gloriously in the attempt
     */
    public static void lockOntoLauncher(RobotController rc, RobotInfo[] enemies) throws GameActionException
    {
        // if we can't move then don't waste bytecodes
        if (!rc.isCoreReady())
        {
            return;
        }
        RobotInfo launcher = null;

        for (int i = enemies.length; --i>=0; )
        {
            if (enemies[i].type == RobotType.LAUNCHER)
            {
                launcher = enemies[i];
                break;
            }
        }

        // if all we can c are missiles
        if (launcher == null)
        {

        }
        // lock on launcher
        else
        {
            Direction dir = rc.getLocation().directionTo(launcher.location);

            // if possible move towards the launcher
            if (rc.canMove(dir))
            {
                rc.move(dir);
            }
            else if (rc.canMove(dir.rotateRight()))
            {
                rc.move(dir.rotateRight());
            }
            else if (rc.canMove(dir.rotateLeft()))
            {
                rc.move(dir.rotateLeft());
            }
        }
    }

    /**
     * We are one move away from enemyTower
     */
    public static boolean enemyTowerClose(RobotController rc, MapLocation[] enemyTowers)
    {
        for (int i = enemyTowers.length; --i>=0; )
        {
            if (rc.getLocation().distanceSquaredTo(enemyTowers[i]) < 37)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * This method determines if any of our allies have engaged
     */
    public static boolean alliesEngaged(RobotInfo[] allies, RobotInfo[] enemies, MapLocation[] enemyTowers)
    {
        for (int i = allies.length; --i>=0; )
        {
            int range = allies[i].type.attackRadiusSquared;
            MapLocation ally = allies[i].location;

            for (int j = enemies.length; --j>=0; )
            {
                MapLocation enemy = enemies[j].location;

                if (ally.distanceSquaredTo(enemy) <= range)
                {
                    return true;
                }
            }

            for (int j = enemyTowers.length; --j>=0; )
            {
                if (ally.distanceSquaredTo(enemyTowers[j]) <= 24)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method determines if we are being strafed by an enemy
     */
    public static boolean enemyKitingUs(RobotController rc, RobotInfo[] enemies)
    {
        MapLocation us = rc.getLocation();
        int range = rc.getType().attackRadiusSquared;

        for (int i = enemies.length; --i>=0; )
        {
            int dist = enemies[i].location.distanceSquaredTo(us);

            if (dist > range)
            {
                int theirRange = enemies[i].type.attackRadiusSquared;
                if (theirRange >= dist)
                {
                    rc.setIndicatorString(2, "Enemy" + enemies[i].location);
                    return true;
                }
            }
        }

        return false;
    }

    //============================ Drone micro methods ==============================\\

    /**
     * This function returns the best direction to retreat in
     */
    public static Direction retreatDir(RobotInfo[] enemies, RobotController rc, MapLocation[] enemyTowers, MapLocation enemyHQ)
    {
        Direction[] dirs = Direction.values();
        Direction best = null;
        MapLocation us = rc.getLocation();
        int score = 0;

        for (int a = enemies.length; --a>=0; )
        {
            score += enemies[a].location.distanceSquaredTo(us);
        }

        for (int i = 0; i < 8; i++)
        {
            if (!rc.canMove(dirs[i]))
            {
                continue;
            }

            MapLocation next = us.add(dirs[i]);
            int dirScore = 0;

            for (int j = enemies.length; --j>=0; )
            {
                dirScore += enemies[j].location.distanceSquaredTo(next);
            }

            if (Utilities.locInRangeOfEnemyTower(next, enemyTowers, enemyHQ))
            {
                // if nxt move is in range of tower don't do it
            }
            else if (dirScore > score)
            {
                score = dirScore;
                best = dirs[i];
            }
        }


        return best;
    }

    /**
     * This function finds the best direction to advance in
     */
    public static Direction advanceDir(RobotController rc, RobotInfo[] enemies, MapLocation[] enemyTowers, MapLocation enemyHQ, boolean safe)
    {
        Direction best = null;
        Direction[] dirs = Direction.values();
        MapLocation us = rc.getLocation();
        int bestScore = -1;

        for (int i = 0; i < 8; i++)
        {
            MapLocation next = us.add(dirs[i]);
            int score = 0;

            for (int j = enemies.length; --j>=0; )
            {
                int distToEnemy = enemies[j].location.distanceSquaredTo(next);
                if (score <= 0 && distToEnemy <= 10)
                {
                    score += 20;
                    j = 0;
                }
                if (enemies[j].type.attackRadiusSquared >= distToEnemy)
                {
                    if (safe || enemies[j].type == RobotType.DRONE)
                    {
                        score -= 100000;
                    }
                    score--;
                }
            }

            if (Utilities.locInRangeOfEnemyTower(next, enemyTowers, enemyHQ))
            {
                // if nxt move is in range of tower don't do it
            }
            else if (score > bestScore)
            {
                bestScore = score;
                best = dirs[i];
            }
        }

        return best;
    }

    /**
     * Checks if there is an enemy in range of us
     */
    public static boolean enemyInRange(RobotController rc, RobotInfo[] enemies)
    {
        MapLocation us = rc.getLocation();

        for (int i = enemies.length; --i>=0; )
        {
            int dist = us.distanceSquaredTo(enemies[i].location);
            // bashers and missiles move then attack
            RobotType enemy = enemies[i].type;
            if (enemy == RobotType.BASHER || enemy == RobotType.MISSILE)
            {
                dist -= 11;
            }
            if (dist <= enemies[i].type.attackRadiusSquared)
            {
                return true;
            }

        }
        return false;
    }



    //==================== Methods for Bashers ========================\\
    public static Direction bestBasherDir(RobotController rc, RobotInfo[] enemies)
    {
        Direction[] dirs = Direction.values();
        int score;
        int bestScore = 0;
        Direction best = null;
        MapLocation current;
        MapLocation us = rc.getLocation();

        for (int i = 0; i < 8; i++)
        {
            current = us.add(dirs[i]);
            score = 0;

            if (rc.canMove(dirs[i]))
            {
                for (int j = enemies.length; --j>=0; )
                {
                    if (current.isAdjacentTo(enemies[j].location))
                    {
                        score++;
                    }
                }

                if (score > bestScore)
                {
                    bestScore = score;
                    best = dirs[i];
                }
            }
        }
        return best;
    }

    public static Direction basherDirSecond(RobotController rc, RobotInfo[] enemies)
    {
        Direction[] dirs = Direction.values();
        int score;
        int bestScore = -99999999;
        Direction best = null;
        MapLocation current;
        MapLocation us = rc.getLocation();

        for (int i = 0; i < 8; i++)
        {
            current = us.add(dirs[i]);
            score = 0;

            if (rc.canMove(dirs[i]))
            {
                for (int j = enemies.length; --j>=0; )
                {
                    score -= current.distanceSquaredTo(enemies[j].location);
                }

                if (score > bestScore)
                {
                    bestScore = score;
                    best = dirs[i];
                }
            }
        }
        return best;
    }
}

