package theSimpleSoldier;

import battlecode.common.*;

public class FightMicro
{
    RobotController rc;
    public FightMicro(RobotController rc)
    {
        this.rc = rc;
    }
    public static Direction[] dirs = Direction.values();

    public boolean basicFightMicro(RobotInfo[] nearByEnemies) throws GameActionException
    {
        if (!rc.isWeaponReady())
        {
            return false;
        }

        if (nearByEnemies.length < 1)
        {
            return false;
        }
        RobotInfo enemyToAttack = findWeakestEnemy(nearByEnemies);
        MapLocation target = enemyToAttack.location;

        if (rc.canAttackLocation(target))
        {
            rc.attackLocation(target);
            return true;
        }
        return false;
    }


    /**
     * This method will attack enemies around it
     */
    public boolean structureFightMicro(RobotInfo[] nearByEnemies) throws GameActionException
    {
        if (!rc.isWeaponReady())
        {
            return false;
        }
        if (nearByEnemies.length < 1)
        {
            return false;
        }
        RobotInfo enemyToAttack = findWeakestEnemy(nearByEnemies);
        MapLocation target = enemyToAttack.location;

        if (rc.canAttackLocation(target))
        {
            rc.attackLocation(target);
            return true;
        }
        return false;
    }


    /**
     * This method returns the RobotInfo for the Robot with the lowest health
     */
    public RobotInfo findWeakestEnemy(RobotInfo[] nearByEnemies)
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
     * This method carrys out a missile's attack
     */
    public void missileAttack() throws GameActionException
    {
        Direction bestDir = Direction.NONE;
        int bestScore;
        MapLocation us = rc.getLocation();

        bestScore = getSpotScore(us);
        int currentScore;
        MapLocation current;

        for (int i = 0; i < 8; i++)
        {
            if (rc.canMove(dirs[i]))
            {
                current = us.add(dirs[i]);
                currentScore = getSpotScore(current);
                if (currentScore > bestScore)
                {
                    bestDir = dirs[i];
                    bestScore = currentScore;
                }
            }
        }

        if (bestDir != Direction.NONE)
        {
            rc.move(bestDir);
        }
        else
        {
            rc.explode();
        }
    }

    /**
     * This is a second attempt with missile fighting
     */
    public MapLocation missileAttack2() throws GameActionException
    {
        RobotInfo[] nearByEnemies = rc.senseNearbyRobots(2, rc.getTeam().opponent());

        if (nearByEnemies.length == 0)
        {
            nearByEnemies = rc.senseNearbyRobots(35, rc.getTeam().opponent());
            if (nearByEnemies.length == 0)
            {
                nearByEnemies = rc.senseNearbyRobots(999, rc.getTeam().opponent());
            }
            return nearByEnemies[0].location;
        }

        RobotInfo[] nearByAllies = rc.senseNearbyRobots(2, rc.getTeam());

        if (nearByEnemies.length > nearByAllies.length * 2)
        {
            rc.explode();
        }
        else
        {
            nearByEnemies = rc.senseNearbyRobots(35, rc.getTeam().opponent());
            if (nearByEnemies.length == 0)
            {
                nearByEnemies = rc.senseNearbyRobots(999, rc.getTeam().opponent());
            }
            return nearByEnemies[0].location;
        }
        return rc.getLocation();
    }

    /**
     * This method calculates the score for a location
     */
    public int getSpotScore(MapLocation spot) throws GameActionException
    {
        int totalScore = 0;
        MapLocation newSpot;
        RobotInfo bot;
        Team us = rc.getTeam();

        for (int i = 8; --i >= 0; )
        {
            newSpot = spot.add(dirs[i]);
            bot = rc.senseRobotAtLocation(newSpot);

            if (bot != null)
            {
                if (bot.team == us)
                {
                    totalScore -= 20;
                }
                else
                {
                    totalScore += 20;
                }
            }
        }
        return totalScore;
    }


    public boolean launcherAttack(RobotInfo[] nearByEnemies) throws GameActionException
    {
        if (!rc.isWeaponReady())
        {
            return false;
        }

        if (rc.getMissileCount() == 0)
        {
            return false;
        }

        if (nearByEnemies.length == 0)
        {
            return false;
        }

        Direction dir = rc.getLocation().directionTo(nearByEnemies[0].location);
        int i = 0;
        while (!rc.canMove(dir) && i < nearByEnemies.length)
        {
            dir = rc.getLocation().directionTo(nearByEnemies[i].location);
        }
        if (rc.canMove(dir))
        {
            rc.launchMissile(dir);
        }
        return true;
    }

}
