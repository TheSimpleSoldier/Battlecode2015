package team044.Units;


import team044.Messaging;
import team044.Navigator;
import team044.Unit;

import battlecode.common.*;
import team044.Utilities;

public class Drone extends Unit
{
    MapLocation target;
    public Drone()
    {
        //default constructor
    }

    public Drone(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfDronesOdd.ordinal(), Messaging.NumbOfDronesEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }
        return nav.takeNextStep(target);
        //return nav.badMovement(target);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basicFightMicro(nearByEnemies);
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
