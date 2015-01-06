package theSimpleSoldier;

import battlecode.common.*;

public class Navigator
{
    private RobotController rc;
    Navigator nav;
    public Navigator(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
    }

    // This method uses a simple Bug algorithm to move
    public void takeNextStep(MapLocation target)
    {
        // TODO: Implement
    }
}
