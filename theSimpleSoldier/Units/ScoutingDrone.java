package theSimpleSoldier.Units;


import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import theSimpleSoldier.Navigator;
import theSimpleSoldier.Unit;
import theSimpleSoldier.Utilities;

public class ScoutingDrone extends Unit
{
    RobotController rc;
    Navigator nav;
    public ScoutingDrone(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        if(Clock.getRoundNum() % 20 == 0)
        {
            Utilities.getBestSpot(rc, false);
        }
    }

    public void handleMessages() throws GameActionException
    {
        // default to doing nothing
    }

    public boolean takeNextStep() throws GameActionException
    {
        return false;
    }

    public boolean fight() throws GameActionException
    {
        return false;
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
