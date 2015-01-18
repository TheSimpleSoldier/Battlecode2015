package team044.Units;

import battlecode.world.Util;
import team044.*;

import java.util.*;

import battlecode.common.*;

public class Commander extends Unit
{
    boolean regenerating = false;
    boolean rushing = false;
    boolean avoidStructures = true;
    RobotInfo[] enemies;
    Random random;
    int choice;
    int round;

    public Commander(RobotController rc)
    {
        super(rc);
        target = enemyHQ;
        random = new Random(rc.getID());

        avoidStructures = true;
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();

        rc.setIndicatorString(0, "Flash Cool down: " + rc.getFlashCooldown());


        enemies = rc.senseNearbyRobots(35, opponent);
        // when we hit 100 health we head back to the battlefield
        if (regenerating && rc.getHealth() >= 150)
        {
            rc.setIndicatorString(1, "Attacking");
            regenerating = false;
        }

        // when our health gets too low we head away from the battlefield
        if (!regenerating && (rc.getHealth() <= 75))
        {
            rc.setIndicatorString(1, "Regenerating");
            regenerating = true;
        }

        // change target every 25 turns
        if (Clock.getRoundNum() % 25 != 0 && (!rc.canSenseLocation(target) || rc.isPathable(rc.getType(), target)))
        {
        }
        else if (rc.getLocation().distanceSquaredTo(enemyHQ) > 1000000)
        {
            target = enemyHQ;
        }
        else
        {
            int choice = random.nextInt(5);

            if (choice == 0)
            {
                target = enemyHQ.add(enemyHQ.directionTo(ourHQ), 6);
            }
            else if (choice == 1)
            {
                target = enemyHQ.add(enemyHQ.directionTo(ourHQ).rotateLeft(), 6);
            }
            else if (choice == 2)
            {
                target = enemyHQ.add(enemyHQ.directionTo(ourHQ).rotateRight(), 6);
            }
            else if (choice == 3)
            {
                target = enemyHQ.add(enemyHQ.directionTo(ourHQ).rotateLeft().rotateLeft(), 6);
            }
            else if (choice == 4)
            {
                target = enemyHQ.add(enemyHQ.directionTo(ourHQ).rotateRight().rotateRight(), 6);
            }
        }
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }
        rc.setIndicatorString(1, "Nav movement");
        if (FightMicroUtilities.commanderBlocked(rc, target))
        {
            if (rc.getFlashCooldown() < 1 && rc.isCoreReady())
            {
                Direction dir = rc.getLocation().directionTo(target);
                MapLocation flashTo = FightMicroUtilities.flashInDir(rc, dir);
                if (flashTo != null)
                {
                    rc.setIndicatorString(2, "Flash to: " + flashTo);
                    rc.castFlash(flashTo);
                    return true;
                }
            }
            else if (rc.getFlashCooldown() >= 1)
            {
                return false;
            }
        }
        // if we are more than 20 square away then flash towards target
        else if (rc.getLocation().distanceSquaredTo(target) > 400)
        {
            if (rc.getFlashCooldown() < 1 && rc.isCoreReady())
            {
                Direction dir = rc.getLocation().directionTo(target);
                MapLocation flashTo = FightMicroUtilities.flashInDir(rc, dir);
                if (flashTo != null)
                {
                    rc.setIndicatorString(2, "Flash to: " + flashTo);
                    rc.castFlash(flashTo);
                    return true;
                }
            }
        }
        return nav.takeNextStep(target);

    }

    public boolean fight() throws GameActionException
    {
        rc.setIndicatorString(1, "Fight Micro");
        return fighter.commanderMicro(nearByEnemies, regenerating, enemies, avoidStructures);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }

    public void distributeSupply() throws GameActionException
    {
    }
}
