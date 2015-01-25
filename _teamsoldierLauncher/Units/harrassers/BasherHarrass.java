package _teamsoldierLauncher.Units.harrassers;

import battlecode.common.*;
import _teamsoldierLauncher.Messaging;
import _teamsoldierLauncher.Unit;
import _teamsoldierLauncher.Units.HarrasserUnit;
import _teamsoldierLauncher.Units.Rushers.BasherRusher;
import _teamsoldierLauncher.Units.Rushers.SoldierRusher;
import _teamsoldierLauncher.Utilities;

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
