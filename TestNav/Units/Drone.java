package TestNav.Units;


import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import TestNav.Messaging;
import TestNav.Unit;
import TestNav.Units.Rushers.DroneRusher;

public class Drone extends DefensiveUnits
{

    public Drone(RobotController rc)
    {
        super(rc);
        nav.setVirtualVoids(false);
    }

    public void collectData2() throws GameActionException
    {
        // nothing special
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
        //return fighter.basicFightMicro(nearByEnemies);
        //return fighter.droneAttack(nearByEnemies);
        return false;
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new DroneRusher(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
