package team044.Units;

import battlecode.world.Util;
import team044.Messaging;
import team044.Navigator;
import team044.Unit;
import java.util.*;

import battlecode.common.*;
import team044.Utilities;

public class Commander extends Unit
{
    boolean regenerating = false;
    boolean rushing = false;
    boolean avoidStructures = true;
    RobotInfo[] enemies;
    Random random;


    public Commander(RobotController rc)
    {
        super(rc);
        nav.setAvoidTowers(false);
        target = ourHQ;
        random = new Random(rc.getID());
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();

        /*if (rc.hasLearnedSkill(CommanderSkillType.FLASH))
        {
            rc.setIndicatorString(0, "Flash Cool down: " + rc.getFlashCooldown());
        }*/

        enemies = rc.senseNearbyRobots(35, opponent);
        // when we hit 100 health we head back to the battlefield
        if (regenerating && rc.getHealth() >= 100)
        {
            rc.setIndicatorString(1, "Attacking");
            regenerating = false;
        }

        // when our health gets too low we head away from the battlefield
        if (!regenerating && (rc.getHealth() <= 60))
        {
            rc.setIndicatorString(1, "Regenerating");
            regenerating = true;
        }

        if (regenerating)
        {
            if (rushing)
            {
                target = Utilities.getRushLocation(rc);
                target = target.add(rc.getLocation().directionTo(ourHQ), 10);
            }
            else
            {
                target = ourHQ.add(enemyHQ.directionTo(ourHQ), 3);
            }

            avoidStructures = true;
            nav.setAvoidHQ(true);
            nav.setAvoidTowers(true);
        }
        else
        {

            if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
            {
                rushing = true;
                target = Utilities.getRushLocation(rc);
                target = target.add(rc.getLocation().directionTo(target), 5);
            }
            else
            {
                avoidStructures = true;
                target = enemyHQ;
                nav.setAvoidTowers(true);
                nav.setAvoidTowers(true);
                /*
                // change target every 25 turns
                if (Clock.getRoundNum() % 25 != 0)
                {
                }
                else
                {
                    int choice = random.nextInt(3);

                    if (choice == 0)
                    {
                        target = ourHQ.add(ourHQ.directionTo(enemyHQ), 8);
                    }
                    else if (choice == 1)
                    {
                        target = ourHQ.add(ourHQ.directionTo(enemyHQ).rotateLeft(), 8);
                    }
                    else
                    {
                        target = ourHQ.add(ourHQ.directionTo(enemyHQ).rotateRight(), 8);
                    }

                }//*/
            }
        }

        RobotInfo[] allies = rc.senseNearbyRobots(24, rc.getTeam());
        if (!regenerating && rc.readBroadcast(Messaging.Attack.ordinal()) == 1 && allies.length >= 3)
        {
            avoidStructures = false;
            nav.setAvoidTowers(false);
            nav.setAvoidHQ(false);
        }
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            MapLocation toBroadcast = rc.getLocation().add(rc.getLocation().directionTo(target), 3);
            rc.broadcast(Messaging.CommanderLocX.ordinal(), toBroadcast.x);
            rc.broadcast(Messaging.CommanderLocY.ordinal(), toBroadcast.y);
        }
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }

        return nav.takeNextStep(target);
    }

    public boolean fight() throws GameActionException
    {
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
}
