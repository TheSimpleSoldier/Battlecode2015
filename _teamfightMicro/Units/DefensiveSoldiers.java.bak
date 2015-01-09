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
                target = rc.getLocation().add(dirs[random.nextInt(8)], 5);
            }
        }
    }
}
