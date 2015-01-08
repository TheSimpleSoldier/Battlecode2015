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

        RobotInfo enemyToAttack = FightMicroUtilities.findWeakestEnemy(nearByEnemies);
        MapLocation target = enemyToAttack.location;

        if (rc.canAttackLocation(target))
        {
            rc.attackLocation(target);
            return true;
        }
        return false;
    }

    /**
     * This is a more sophisticated fight micro for tanks/soldiers/miners/beavers/commanders
     */
    public boolean advancedFightMicro(RobotInfo[] nearByEnemies) throws GameActionException
    {
        boolean move = true;
        // if we can't move then skip to shooting part
        if (!rc.isCoreReady())
        {
            // guess we can't do much except maybe shoot
            move = false;
        }
        else
        {
            MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
            // search for enemies in sight range
            RobotInfo[] enemies = rc.senseNearbyRobots(24, rc.getTeam().opponent());

            // if their are no enemies we can't fight
            if (enemies.length == 0)
            {
                if (FightMicroUtilities.enemyTowerClose(rc, enemyTowers))
                {
                    RobotInfo[] allies = rc.senseNearbyRobots(24, rc.getTeam());
                    int balance = FightMicroUtilities.balanceOfPower(enemies, allies);

                    // if we have health advantage press forward
                    if (balance > 1100)
                    {
                        Direction dir = rc.getLocation().directionTo(Utilities.closestTower(rc, enemyTowers));
                        if (rc.canMove(dir))
                        {
                            rc.move(dir);
                        }

                    }
                    // else wait
                    else
                    {
                        // wait
                    }
                    return true;
                }
                return false;
            }

            // search for allies in sight range
            RobotInfo[] allies = rc.senseNearbyRobots(24, rc.getTeam());

            int balance = FightMicroUtilities.balanceOfPower(enemies, allies);
            MapLocation closestTower = Utilities.closestTower(rc, enemyTowers);
            int dist = rc.getLocation().distanceSquaredTo(closestTower);

            if (dist > 24 && dist < 36)
            {
                balance -= 1000;
            }

            // if enemy is more powerful retreat
            if (balance < -50)
            {
                FightMicroUtilities.retreat(rc, enemies);

            }
            // if we are against launchers just die
            else if (FightMicroUtilities.enemyHasLaunchers(enemies))
            {
                FightMicroUtilities.lockOntoLauncher(rc, enemies);
            }
            // If we are evenly matched stand ground
            else if (balance < 50)
            {
                // hold position shoot enemies that approach
            }
            // if we have the advantage Charge
            else
            {
                if (nearByEnemies.length > 0)
                {
                    // hold ground and shoot
                }
                else
                {
                    FightMicroUtilities.attack(rc, enemies);
                }
            }
        }

        // if we don't have weapon delay and are in range Attack!!!
        if (rc.isWeaponReady() && nearByEnemies.length > 0)
        {
            RobotInfo enemyToAttack = FightMicroUtilities.prioritizeTargets(nearByEnemies);
            MapLocation target = enemyToAttack.location;

            if (rc.canAttackLocation(target))
            {
                rc.attackLocation(target);
            }
        }
        // if we can't shoot and didn't move when we could
        else if (!rc.isWeaponReady() && rc.isCoreReady())
        {
            return false;
        }
        // if we couldn't move or shoot
        else if (!move)
        {
            return false;
        }

        return true;
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
        RobotInfo enemyToAttack = FightMicroUtilities.findWeakestEnemy(nearByEnemies);
        MapLocation target = enemyToAttack.location;

        if (rc.canAttackLocation(target))
        {
            rc.attackLocation(target);
            return true;
        }
        return false;
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

}
