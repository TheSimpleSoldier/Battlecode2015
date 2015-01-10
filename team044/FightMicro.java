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
        rc.setIndicatorString(0, "running fight micro");
        boolean move = true;
        // if we can't move then skip to shooting part
        if (!rc.isCoreReady())
        {
            // guess we can't do much except maybe shoot
            move = false;
        }
        else {
            MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
            // search for enemies in sight range
            RobotInfo[] enemies = rc.senseNearbyRobots(35, rc.getTeam().opponent());

            // search for allies in sight range
            RobotInfo[] allies = rc.senseNearbyRobots(24, rc.getTeam());

            int balance = FightMicroUtilities.balanceOfPower(enemies, allies);
            MapLocation closestTower = Utilities.closestTower(rc, enemyTowers);
            int dist;
            if (closestTower != null)
            {
                dist = rc.getLocation().distanceSquaredTo(closestTower);
            }
            else
            {
                dist = 9999;
            }

            if (dist > 24 && dist < 36)
            {
                balance -= 3000;
            }

            balance += rc.getHealth() * rc.getType().attackPower;

            // if their are no enemies we can't fight
            if (nearByEnemies.length == 0)
            {
                if (FightMicroUtilities.enemyTowerClose(rc, enemyTowers))
                {
                    // if we have health advantage press forward
                    if (balance > 500)
                    {
                        rc.setIndicatorString(1, "balance > 500");
                        Direction dir = rc.getLocation().directionTo(Utilities.closestTower(rc, enemyTowers));
                        if (rc.canMove(dir))
                        {
                            rc.move(dir);
                        }
                    }
                    else if (FightMicroUtilities.alliesEngaged(allies, enemies, enemyTowers))
                    {
                        rc.setIndicatorString(1, "Allies Engaged");
                        FightMicroUtilities.attack(rc, enemies);
                        return true;
                    }
                    else if (FightMicroUtilities.enemyKitingUs(rc, enemies))
                    {
                        rc.setIndicatorString(1, "enemyKiting");
                        FightMicroUtilities.attack(rc, enemies);
                        return true;
                    }
                    // else wait
                    else
                    {
                        // wait
                    }
                    return true;
                }
                else if (FightMicroUtilities.enemyKitingUs(rc, enemies))
                {
                    rc.setIndicatorString(1, " Enemy kiting us");
                    FightMicroUtilities.attack(rc, enemies);
                }
                return false;
            }

            // there are enemies in range of us

            // if enemy is significantly more powerful retreat
            // unless we are near a tower in which we will die before we get out of range
            if (FightMicroUtilities.alliesEngaged(allies, enemies, enemyTowers))
            {
                // stand your ground!!!
            }
            else if (balance < -500 && dist > 24)
            {
                rc.setIndicatorString(1, "retreat!");
                FightMicroUtilities.retreat(rc, enemies);

            }
            // if we are against launchers just die ;)
            // we focus on killing the enemy launcher instead of shooting at missiles
            else if (FightMicroUtilities.enemyHasLaunchers(enemies))
            {
                rc.setIndicatorString(1, "enemy launchers");
                FightMicroUtilities.lockOntoLauncher(rc, enemies);
            }

            // otherwise we shoot
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
