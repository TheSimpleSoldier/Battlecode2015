package team044;

import battlecode.common.*;

public class Messenger
{
    RobotController rc;
    private int numbOfBashers = 0;
    private int numbOfComputers = 0;
    private int numbOfLaunchers = 0;
    private int numbOfMiners = 0;
    private int numbOfSoldiers = 0;
    private int numbOfTanks = 0;
    private int numbOfDrones = 0;

    private BuildOrderMessaging[] basherStrat;
    private BuildOrderMessaging[] computerStrat;
    private BuildOrderMessaging[] launcherStrat;
    private BuildOrderMessaging[] minerStrat;
    private BuildOrderMessaging[] soldierStrat;
    private BuildOrderMessaging[] tankStrat;
    private BuildOrderMessaging[] droneStrat;

    public Messenger(RobotController rc) throws GameActionException
    {
        this.rc = rc;
        // initialize everything to -1
        rc.broadcast(Messaging.TankType.ordinal(), -1);
        rc.broadcast(Messaging.SoldierType.ordinal(), -1);
        rc.broadcast(Messaging.ComputerType.ordinal(), -1);
        rc.broadcast(Messaging.LauncherType.ordinal(), -1);
        rc.broadcast(Messaging.MinerType.ordinal(), -1);
        rc.broadcast(Messaging.BasherType.ordinal(), -1);
        rc.broadcast(Messaging.DroneType.ordinal(), -1);

        // initialize strategies
        basherStrat = new BuildOrderMessaging[1];
        basherStrat[0] = BuildOrderMessaging.BuildHarrassBasher;

        computerStrat = new BuildOrderMessaging[1];
        computerStrat[0] = BuildOrderMessaging.BuildComputer;

        launcherStrat = new BuildOrderMessaging[1];
        launcherStrat[0] = BuildOrderMessaging.BuildLauncher;

        minerStrat = new BuildOrderMessaging[1];
        minerStrat[0] = BuildOrderMessaging.BuildMiner;

        soldierStrat = new BuildOrderMessaging[1];
        soldierStrat[0] = BuildOrderMessaging.BuildHarrassSoldier;

        tankStrat = new BuildOrderMessaging[1];
        tankStrat[0] = BuildOrderMessaging.BuildHarrassTank;

        droneStrat = new BuildOrderMessaging[1];
        droneStrat[0] = BuildOrderMessaging.BuildSearchAndDestroyDrone;
    }

    /**
     * When a Unit gets its orders it changes that channel to -1
     * so that the HQ knows to issue orders for the next unit
     */
    public void giveUnitOrders() throws GameActionException
    {
        // we want to give a little time before we start managing supply distribution
        if (rc.readBroadcast(Messaging.NumbOfDrones.ordinal()) < 1)
        {
            droneStrat[0] = BuildOrderMessaging.BuildSupplyDrone;
        }
        else
        {
            droneStrat[0] = BuildOrderMessaging.BuildSearchAndDestroyDrone;
        }

        int message;
        if (rc.readBroadcast(Messaging.BasherType.ordinal()) == -1)
        {
            message = basherStrat[numbOfBashers].ordinal();
            rc.broadcast(Messaging.BasherType.ordinal(), message);
            numbOfBashers = (numbOfBashers + 1) % basherStrat.length;
        }

        if (rc.readBroadcast(Messaging.ComputerType.ordinal()) == -1)
        {
            message = computerStrat[numbOfComputers].ordinal();
            rc.broadcast(Messaging.ComputerType.ordinal(), message);
            numbOfComputers = (numbOfComputers + 1) % computerStrat.length;
        }

        if (rc.readBroadcast(Messaging.LauncherType.ordinal()) == -1)
        {
            message = launcherStrat[numbOfLaunchers].ordinal();
            rc.broadcast(Messaging.LauncherType.ordinal(), message);
            numbOfLaunchers = (numbOfLaunchers + 1) % launcherStrat.length;
        }

        if (rc.readBroadcast(Messaging.MinerType.ordinal()) == -1)
        {
            message = minerStrat[numbOfMiners].ordinal();
            rc.broadcast(Messaging.MinerType.ordinal(), message);
            numbOfMiners = (numbOfMiners + 1) % minerStrat.length;
        }

        if (rc.readBroadcast(Messaging.SoldierType.ordinal()) == -1)
        {
            message = soldierStrat[numbOfSoldiers].ordinal();
            rc.broadcast(Messaging.SoldierType.ordinal(), message);
            numbOfSoldiers = (numbOfSoldiers + 1) % soldierStrat.length;
        }

        if (rc.readBroadcast(Messaging.TankType.ordinal()) == -1)
        {
            message = tankStrat[numbOfTanks].ordinal();
            rc.broadcast(Messaging.TankType.ordinal(), message);
            numbOfTanks = (numbOfTanks + 1) % tankStrat.length;
        }

        if (rc.readBroadcast(Messaging.DroneType.ordinal()) == -1)
        {
            message = droneStrat[numbOfDrones].ordinal();
            rc.broadcast(Messaging.DroneType.ordinal(), message);
            numbOfDrones = (numbOfDrones + 1) % droneStrat.length;
        }
    }


    /**
     * This functions allow the HQ to change the unit strategies
     * throughout the course of the game
     */
    public void changeBasherStrat(BuildOrderMessaging[] basherStrat)
    {
        this.basherStrat = basherStrat;
    }

    public void changeComputerStrat(BuildOrderMessaging[] computerStrat)
    {
        this.computerStrat = computerStrat;
    }

    public void changeLauncherStrat(BuildOrderMessaging[] launcherStrat)
    {
        this.launcherStrat = launcherStrat;
    }

    public void changeMinerStrat(BuildOrderMessaging[] minerStrat)
    {
        this.minerStrat = minerStrat;
    }

    public void changeSoldierStrat(BuildOrderMessaging[] soldierStrat)
    {
        this.soldierStrat = soldierStrat;
    }

    public void changeTankStrat(BuildOrderMessaging[] tankStrat)
    {
        this.tankStrat = tankStrat;
    }

    public void changeDroneStrat(BuildOrderMessaging[] droneStrat)
    {
        this.droneStrat = droneStrat;
    }
}
