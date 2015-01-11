package team044.Units;

import battlecode.common.*;
import team044.Messaging;
import team044.Unit;

public abstract class DefensiveUnits extends Unit
{
    public DefensiveUnits(RobotController rc)
    {
        super(rc);
    }

    public void collectData() throws GameActionException
    {
        super.collectData();

        int x = rc.readBroadcast(Messaging.BuildingInDistressX.ordinal());
        int y = rc.readBroadcast(Messaging.BuildingInDistressY.ordinal());

        boolean defendBuilding = false;
        if (x != 0 && y != 0)
        {
            MapLocation building = new MapLocation(x,y);

            if (building.distanceSquaredTo(rc.getLocation()) < 200)
            {
                target = building;
                defendBuilding = true;
            }
        }

        int index = rc.readBroadcast(Messaging.TowerUnderAttack.ordinal()) - 1;
        if (index >= 0)
        {
            MapLocation[] towers = rc.senseTowerLocations();
            MapLocation tower = towers[index];

            // go farther for towers
            if (rc.getLocation().distanceSquaredTo(tower) < 400)
            {
                target = tower;
                defendBuilding = true;
            }
        }


        // if we are not defending a building run unit specified code
        if (!defendBuilding)
        {
            collectData2();
        }
    }

    // method for children to call to determine movement if they don't rush to help
    public abstract void collectData2() throws GameActionException;

    public boolean fight() throws GameActionException
    {
        return fighter.advancedFightMicro(nearByEnemies);
    }

    public boolean takeNextStep() throws GameActionException
    {
        if (target == null)
        {
            return false;
        }
        return nav.takeNextStep(target);
    }
}
