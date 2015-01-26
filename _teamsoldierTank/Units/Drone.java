package _teamsoldierTank.Units;


import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import _teamsoldierTank.BuildOrderMessaging;
import _teamsoldierTank.Messaging;
import _teamsoldierTank.Unit;
import _teamsoldierTank.Units.Followers.DroneFollower;
import _teamsoldierTank.Units.Rushers.DroneRusher;

public class Drone extends DefensiveUnits
{

    public Drone(RobotController rc)
    {
        super(rc);
        nav.setCircle(true);
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
        return fighter.droneAttack(nearByEnemies);
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        int type = rc.readBroadcast(Messaging.DroneType.ordinal());
        rc.broadcast(Messaging.DroneType.ordinal(), -1);
        if (rc.readBroadcast(Messaging.RushEnemyBase.ordinal()) == 1)
        {
            return new DroneRusher(rc);
        }
        else if (type == BuildOrderMessaging.BuildFollowerDrone.ordinal())
        {
            return new DroneFollower(rc);
        }
        return current;
    }

    public boolean carryOutAbility() throws GameActionException
    {
        return false;
    }
}
