package team044.Units;

import team044.Messaging;
import team044.Navigator;
import team044.Unit;
import battlecode.common.*;
import team044.Utilities;

public class Computer extends Unit
{
    public Computer(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfCompsOdd.ordinal(), Messaging.NumbOfCompsEven.ordinal());
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
