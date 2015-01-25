package _teamSoldierTank.Units.harrassers;

import battlecode.common.*;
import _teamSoldierTank.Messaging;
import _teamSoldierTank.Unit;
import _teamSoldierTank.Units.HarrasserUnit;
import _teamSoldierTank.Units.Rushers.BasherRusher;
import _teamSoldierTank.Units.Rushers.SoldierRusher;
import _teamSoldierTank.Utilities;

public class BasherHarrass extends HarrasserUnit
{
    public BasherHarrass(RobotController rc)
    {
        super(rc);
        nav.setAvoidHQ(true);
        nav.setAvoidTowers(true);
    }

    public void handleMessages() throws GameActionException
    {
        super.handleMessages();

        Utilities.handleMessageCounter(rc, Messaging.NumbOfBashersOdd.ordinal(), Messaging.NumbOfBashersEven.ordinal());
    }

    public Unit getNewStrategy(Unit current) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.Attack.ordinal()) == 1)
        {
            return new BasherRusher(rc);
        }
        return current;
    }

    public boolean fight() throws GameActionException
    {
        return fighter.basherFightMicro();
    }
}
