package _teambasherSoldier.Units.Defenders;

import battlecode.common.*;
import _teambasherSoldier.Units.DefensiveUnits;
import _teambasherSoldier.*;
import _teambasherSoldier.Units.Rushers.TankRusher;
import _teambasherSoldier.Units.Tank;

import java.util.Random;

public class DefensiveTank extends DefensiveUnits
{
    private Random random;
    boolean mineDefender = false;
    public DefensiveTank(RobotController rc)
    {
        super(rc);
        rc.setIndicatorString(0, "Defensive Tank");
        random = new Random(rc.getID());

        if (rc.getID() % 3 == 0)
        {
            mineDefender = true;
        }
    }

    public void collectData2() throws GameActionException
    {
        /*if (mineDefender)
        {
            do {
                target = rc.getLocation().add(dirs[random.nextInt(8)], 3);
            } while (!rc.isPathable(RobotType.TANK, target));
        }
        else
        {
            target = Utilities.getTowerClosestToEnemyHQ(rc);
        }*/

        target = Utilities.getTowerClosestToEnemyHQ(rc);

        rc.setIndicatorString(1, "Target: " + target);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new TankRusher(rc);
        }
        return current;
    }
}
