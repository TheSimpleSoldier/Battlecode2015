package team044.Units;

import battlecode.common.*;
import java.util.*;

public class DefensiveSoldiers extends Soldier
{
    private Random random;
    public DefensiveSoldiers(RobotController rc)
    {
        super(rc);
        random = new Random(rc.getID());
        rc.setIndicatorString(0, "Defensive Soldier");
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        if (nearByEnemies.length > 0)
        {
            target = nearByEnemies[0].location;
        }
        else
        {
            Direction dirs[] = Direction.values();

            if (random.nextInt(3) < 2)
            {
                target = rc.getLocation().add(rc.getLocation().directionTo(enemyHQ), 5);
            }
            else
            {
                do {
                    target = rc.getLocation().add(dirs[random.nextInt(8)], 5);
                } while (!rc.isPathable(RobotType.SOLDIER, target));
            }
        }

        rc.setIndicatorString(1, "Target: " + target);
    }
}
