package team044.Units;

import team044.Navigator;
import team044.Unit;

import battlecode.common.*;

public class Commander extends Unit
{
    boolean regenerating = false;
    public Commander(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();

        // when we hit 100 health we head back to the battlefield
        if (regenerating && rc.getHealth() >= 100)
        {
            regenerating = false;
        }

        // when our health gets too low we head away from the battlefield
        if (!regenerating && rc.getHealth() < 30)
        {
            regenerating = true;
        }

        if (regenerating)
        {
            target = ourHQ;
        }
        else
        {
            target = enemyHQ;
        }
    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
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
        return fighter.commanderMicro(nearByEnemies, regenerating);
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
