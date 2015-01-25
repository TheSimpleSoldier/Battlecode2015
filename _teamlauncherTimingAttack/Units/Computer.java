package _teamlauncherTimingAttack.Units;

import _teamlauncherTimingAttack.*;
import battlecode.common.*;

public class Computer extends Unit
{
    boolean scanning;
    MapDiscovery map;
    int job;
    public Computer(RobotController rc) throws GameActionException
    {
        super(rc);
        map = new MapDiscovery();
        scanning = true;
        rc.broadcast(Messaging.NumbOfComps.ordinal(), 1);
        job = 0;

        MapLocation[] towers = rc.senseTowerLocations();
        target = enemyHQ;
        MapLocation myLoc = rc.getLocation();
        for (int i = 0; i < towers.length; i++)
        {
            if (towers[i].distanceSquaredTo(myLoc) < target.distanceSquaredTo(myLoc))
                target = towers[i];
        }
        if (target.equals(enemyHQ)) {
            target = myLoc.add(myLoc.directionTo(ourHQ).opposite());
            target = target.add(myLoc.directionTo(ourHQ).opposite());
            target = target.add(myLoc.directionTo(ourHQ).opposite());
        }
    }

    public void collectData() throws GameActionException
    {
        // collect our data
        super.collectData();
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();
        rc.broadcast(Messaging.ComputerOnline.ordinal(), 1);
        int broadcast = rc.readBroadcast(Messaging.ComputerOnline.ordinal());
        switch (broadcast)
        {
            case 0:
                rc.broadcast(Messaging.ComputerOnline.ordinal(), 1);
                map.checkMap(rc);
                break;
            case 1:
                rc.broadcast(Messaging.ComputerOnline.ordinal(), 2);
                job = 1;
                break;

        }
        Utilities.handleMessageCounter(rc, Messaging.NumbOfCompsOdd.ordinal(), Messaging.NumbOfCompsEven.ordinal());
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null || target.equals(rc.getLocation()))
        {
            return false;
        }
        return nav.takeNextStep(target);
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
        map.checkMap(rc);
        return false;
    }
}
