package theSimpleSoldier;

import battlecode.common.*;

public class Navigator
{
    private RobotController rc;
    public Navigator(RobotController rc)
    {
        this.rc = rc;
    }

    // This method uses a simple Bug algorithm to move
    public void takeNextStep(MapLocation target)
    {
        // TODO: Implement
    }

    public void badMovement(MapLocation target) throws GameActionException
    {
        Direction dir = rc.getLocation().directionTo(target);
        if (rc.canMove(dir))
        {
            rc.move(dir);
        }
        else
        {
            while (!rc.canMove(dir))
            {
                dir = dir.rotateRight();
            }
            rc.move(dir);
        }
    }
}
