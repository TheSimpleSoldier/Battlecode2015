package _teamsoldierTank.Units.Defenders;

import battlecode.common.*;
import _teamsoldierTank.Messaging;
import _teamsoldierTank.Unit;
import _teamsoldierTank.Units.DefensiveUnits;
import _teamsoldierTank.Units.Rushers.SoldierRusher;
import _teamsoldierTank.Units.Soldier;
import _teamsoldierTank.Utilities;

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
        /*
        if (random.nextInt(3) < 2)
        {
            target = rc.getLocation().add(rc.getLocation().directionTo(enemyHQ), 3);
        }
        else
        {
            do {
                target = rc.getLocation().add(dirs[random.nextInt(8)], 3);
            } while (!rc.isPathable(RobotType.SOLDIER, target));
        }*/

        target = Utilities.getTowerClosestToEnemyHQ(rc);


        rc.setIndicatorString(1, "Target: " + target);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        // change to base Basher when it is time to attack
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new SoldierRusher(rc);
        }
        return current;
    }
}
