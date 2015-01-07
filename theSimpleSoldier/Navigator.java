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

    public boolean badMovement(MapLocation target) throws GameActionException
    {
        if (!rc.isCoreReady())
        {
            return false;
        }

        if (rc.getLocation().equals(target))
        {
            return false;
        }

        Direction dir = rc.getLocation().directionTo(target);
        if (rc.canMove(dir))
        {
            rc.move(dir);
            return true;
        }
        else
        {
            while (!rc.canMove(dir))
            {
                dir = dir.rotateRight();
            }
            rc.move(dir);
            return true;
        }
    }
}
