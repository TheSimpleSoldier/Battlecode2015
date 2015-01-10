package team044;

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
            MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();

            for (int i = 0; i < enemyTowers.length; i++)
            {
                if (rc.getLocation().distanceSquaredTo(enemyTowers[i]) < 49)
                {
                    Direction dir = rc.getLocation().directionTo(enemyTowers[i]);
                    if (rc.canMove(dir))
                    {
                        rc.launchMissile(dir);
                        return true;
                    }
                }
            }

            if (rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()) < 49)
            {
                Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
                if (rc.canMove(dir))
                {
                    rc.launchMissile(dir);
                    return true;
                }
            }

            return false;
        }

        Direction dir = rc.getLocation().directionTo(nearByEnemies[0].location);
        /*int i = 0;
        while (!rc.canMove(dir) && i < nearByEnemies.length)
        {
            dir = rc.getLocation().directionTo(nearByEnemies[i].location);
        }*/
        if (rc.canMove(dir))
        {
            rc.launchMissile(dir);
        }

        dir = dir.opposite();
        if (rc.canMove(dir) && rc.isCoreReady())
        {
            rc.move(dir);
        }

        return true;
    }


    public boolean droneAttack(RobotInfo[] nearByEnemies) throws GameActionException
    {
        Direction direction;
        RobotInfo[] enemies = rc.senseNearbyRobots(35, rc.getTeam().opponent());
        // if we can shoot
        if (rc.isWeaponReady())
        {
            // there is no one to shoot
            if (nearByEnemies.length == 0)
            {
                if (enemies.length == 0)
                {
                    return false;
                }
                // there is an enemy outside of shooting range that we can see
                else
                {
                    // if the enemy outranges us run away
                    if ()
                    {

                    }
                    // otherwise advance
                    else
                    {

                    }
                }
            }
            // there are enemies in range of us
            else
            {
                // if there is an enemy that out can shoot us
                // shoot him and back up out of his range
                if ()
                {

                }
                // otherwise just sit and blast him!
                else
                {

                }
            }
        }
        // we can't shoot
        else
        {
            // if we don't see any enemies then no fight micro
            if (enemies.length == 0)
            {
                return false;
            }
            else
            {
                // if we are in range of an enemy retreat
                if ()
                {

                }
                // if there are no enemies in range of us
                else if (nearByEnemies.length == 0)
                {
                    // if we can advance to a location that is not in range of an enemy do so
                    if ()
                    {

                    }
                    // otherwise we sit and do nothing
                }
            }
        }


        // if we can move then do so
        if (rc.canMove(direction))
        {
            rc.move(direction);
        }


        return false;
    }
}
