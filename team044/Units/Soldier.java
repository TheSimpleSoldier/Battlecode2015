package team044.Units;

import team044.Navigator;
import team044.Unit;

import battlecode.common.*;

public class Soldier extends Unit
{
    RobotController rc;
    Navigator nav;
    public Soldier(RobotController rc)
    {
        this.rc = rc;
        nav = new Navigator(rc);
    }

    public void collectData()
    {
        // collect our data
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
