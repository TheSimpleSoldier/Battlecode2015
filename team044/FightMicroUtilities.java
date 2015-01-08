package team044;

import battlecode.common.*;

public class FightMicroUtilities {

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

    /**
     * This method determines if the enemy is more powerful than us
     */
    public static int balanceOfPower(RobotInfo[] enemies, RobotInfo[] allies)
    {
        int alliedHealth = 0;
        int enemyHealth = 0;

        for (int i = allies.length; --i>=0; )
        {
            alliedHealth += allies[i].health;
        }

        for (int j = enemies.length; --j>=0; )
        {
            enemyHealth += enemies[j].health;
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

}
