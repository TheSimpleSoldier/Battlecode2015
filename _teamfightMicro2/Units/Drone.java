package _teamfightMicro2.Units;


import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import _teamfightMicro2.Unit;

public class Drone extends Unit
{
    public MapLocation target;
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
        return fighter.droneAttack(nearByEnemies);
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