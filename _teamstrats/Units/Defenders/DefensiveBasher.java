package _teamstrats.Units.Defenders;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import _teamstrats.Units.Basher;
import _teamstrats.Units.DefensiveUnits;
import _teamstrats.*;

public class DefensiveBasher extends DefensiveUnits
{
    public DefensiveBasher(RobotController rc)
    {
        super(rc);
    }

    public void collectData2() throws GameActionException
    {
        target = Utilities.getTowerClosestToEnemyHQ(rc);


        rc.setIndicatorString(1, "Target: " + target);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basherFightMicro();
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        // change to base Basher when it is time to attack
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new Basher(rc);
        }
        return current;
    }
}
