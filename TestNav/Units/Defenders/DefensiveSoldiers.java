package TestNav.Units.Defenders;

import battlecode.common.*;
import TestNav.Messaging;
import TestNav.Units.DefensiveUnits;
import TestNav.Utilities;

import java.util.*;

public class DefensiveSoldiers extends DefensiveUnits
{
    private Random random;

    public DefensiveSoldiers(RobotController rc)
    {
        super(rc);
        random = new Random(rc.getID());
        rc.setIndicatorString(0, "Defensive Soldier");
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfSoldiersOdd.ordinal(), Messaging.NumbOfSoldiersEven.ordinal());
    }

    public void collectData2() throws GameActionException
    {
        if (random.nextInt(3) < 2)
        {
            target = rc.getLocation().add(rc.getLocation().directionTo(enemyHQ), 3);
        }
        else
        {
            do {
                target = rc.getLocation().add(dirs[random.nextInt(8)], 3);
            } while (!rc.isPathable(RobotType.SOLDIER, target));
        }


        rc.setIndicatorString(1, "Target: " + target);
    }
}