package team044;

import team044.Units.*;
import team044.Structures.*;
import battlecode.common.*;

public class RobotPlayer
{
    private static Unit unit;
    public static void run(RobotController rc)
    {
        while (true)
        {
            try
            {
                // units
                if (rc.getType() == RobotType.BEAVER)
                {
                    unit = getBeaver(rc);
                }
                else if (rc.getType() == RobotType.COMPUTER)
                {
                    unit = getComputer(rc);
                }
                else if (rc.getType() == RobotType.COMMANDER)
                {
                    unit = getCommander(rc);
                }
                else if (rc.getType() == RobotType.SOLDIER)
                {
                    unit = getSoldier(rc);
                }
                else if (rc.getType() == RobotType.BASHER)
                {
                    unit = getBasher(rc);
                }
                else if (rc.getType() == RobotType.TANK)
                {
                    unit = getTank(rc);
                }
                else if (rc.getType() == RobotType.DRONE)
                {
                    unit = getDrone(rc);
                }
                else if (rc.getType() == RobotType.LAUNCHER)
                {
                    unit = getLauncher(rc);
                }
                else if (rc.getType() == RobotType.MINER)
                {
                    unit = getMiner(rc);
                }
                // Structures
                else if (rc.getType() == RobotType.AEROSPACELAB)
                {
                    unit = getAerospaceLab(rc);
                }
                else if (rc.getType() == RobotType.BARRACKS)
                {
                    unit = getBarracks(rc);
                }
                else if (rc.getType() == RobotType.HELIPAD)
                {
                    unit = getHelipad(rc);
                }
                else if (rc.getType() == RobotType.MINERFACTORY)
                {
                    unit = getMinerFactory(rc);
                }
                else if (rc.getType() == RobotType.TANKFACTORY)
                {
                    unit = getTankFactory(rc);
                }
                else if (rc.getType() == RobotType.TECHNOLOGYINSTITUTE)
                {
                    unit = getTechnologyInstitute(rc);
                }
                else if (rc.getType() == RobotType.TRAININGFIELD)
                {
                    unit = getTrainingField(rc);
                }
                else if (rc.getType() == RobotType.HQ)
                {
                    unit = getHQ(rc);
                }
                else if (rc.getType() == RobotType.TOWER)
                {
                    unit = getTower(rc);
                    Utilities.getBestSpot(rc, false);
                }
                else if (rc.getType() == RobotType.MISSILE)
                {
                    unit = getMissile(rc);
                }
                else
                {
                    System.out.println("Houston we have a problem");
                }

                while (true)
                {
                    try
                    {
                        unit.collectData();
                        unit.handleMessages();
                        if (unit.fight())
                        {
                            // run fight micro
                        }
                        if (unit.carryOutAbility())
                        {
                            // execute ability
                        }
                        if (unit.takeNextStep())
                        {
                            // take one step forward
                        }

                        unit = unit.getNewStrategy(unit);

                        unit.distributeSupply();
                        rc.yield();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        rc.yield();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Failure setting unit type");
            }
        }
    }

    // the methods below determine the class we use for a particular unit

    private static Unit getBeaver(RobotController rc) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.BeaverType.ordinal()) == BuildOrderMessaging.BuildBeaverBuilder.ordinal())
        {
            return new BuildingBeaver(rc);
        }
        else if (rc.readBroadcast(Messaging.BeaverType.ordinal()) == BuildOrderMessaging.BuildBeaverMiner.ordinal())
        {
            return new MinerBeaver(rc);
        }
        return new Beaver(rc);
    }

    private static Unit getComputer(RobotController rc)
    {
        return new Computer(rc);
    }

    private static Unit getCommander(RobotController rc)
    {
        return new Commander(rc);
    }

    private static Unit getSoldier(RobotController rc)
    {
        return new Soldier(rc);
    }

    private static Unit getBasher(RobotController rc)
    {
        return new Basher(rc);
    }

    private static Unit getTank(RobotController rc)
    {
        return new Tank(rc);
    }

    private static Unit getDrone(RobotController rc) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.DroneType.ordinal()) == BuildOrderMessaging.BuildScoutingDrone.ordinal())
        {
            return new ScoutingDrone(rc);
        }
        return new Drone(rc);
    }

    private static Unit getLauncher(RobotController rc)
    {
        return new Launcher(rc);
    }

    private static Unit getMiner(RobotController rc)
    {
        return new Miner(rc);
    }

    private static Unit getAerospaceLab(RobotController rc)
    {
        return new AerospaceLab(rc);
    }

    private static Unit getBarracks(RobotController rc)
    {
        return new Barracks(rc);
    }

    private static Unit getHelipad(RobotController rc)
    {
        return new Helipad(rc);
    }

    private static Unit getMinerFactory(RobotController rc)
    {
        return new MinerFactory(rc);
    }

    private static Unit getTankFactory(RobotController rc)
    {
        return new TankFactory(rc);
    }

    private static Unit getTechnologyInstitute(RobotController rc)
    {
        return new TechnologyInstitute(rc);
    }

    private static Unit getTrainingField(RobotController rc)
    {
        return new TrainingField(rc);
    }

    private static Unit getHQ(RobotController rc) throws GameActionException
    {
        return new HQ(rc);
    }

    private static Unit getTower(RobotController rc)
    {
        return new Tower(rc);
    }

    private static Unit getMissile(RobotController rc)
    {
        return new Missile(rc);
    }
}