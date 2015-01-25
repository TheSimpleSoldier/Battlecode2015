package _teambasherSoldier.Units.harrassers;

import battlecode.common.*;
import _teambasherSoldier.Messaging;
import _teambasherSoldier.Unit;
import _teambasherSoldier.Units.HarrasserUnit;
import _teambasherSoldier.Units.Rushers.BasherRusher;
import _teambasherSoldier.Units.Rushers.SoldierRusher;
import _teambasherSoldier.Utilities;

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
