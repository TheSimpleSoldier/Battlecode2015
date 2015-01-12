package team044.Structures;

import battlecode.common.*;
import team044.FightMicro;
import team044.Messaging;
import team044.Structure;
import team044.TeamMemory;

public class Tower extends Structure
{
    FightMicro fighter;
    RobotInfo[] enemies;
    RobotInfo[] nearByEnemies;
    RobotInfo[] allies;
    RobotInfo[] nearByAllies;
    public Tower(RobotController rc)
    {
        super(rc);
        fighter = new FightMicro(rc);
    }

    public void handleMessages() throws GameActionException
    {
        if (nearByEnemies.length > 0)
        {
            MapLocation[] towers = rc.senseTowerLocations();
            MapLocation us = rc.getLocation();

            for (int i = 0; i < towers.length; i++)
            {
                if (towers[i] == us)
                {
<<<<<<< HEAD
                    rc.broadcast(Messaging.TowerUnderAttack.ordinal(), i);

                    // Set team memory on first attack only.
                    if (rc.readBroadcast(Messaging.AttackOccurred.ordinal()) == 0 && rc.getHealth() < RobotType.TOWER.maxHealth)
                    {
                        rc.broadcast(Messaging.AttackOccurred.ordinal(), 1);
                        int[] enemyType = new int[5];
                        int enemyCountMax = -1;
                        int mostUnits = -1;
                        int secondMost = 0;
                        for (int j = 0; j < nearByEnemies.length; j++)
                        {
                            RobotType typeCheck = nearByEnemies[j].type;
                            // Drone counter
                            if (typeCheck.equals(RobotType.DRONE))
                            {
                                enemyType[0]++;
                                if (enemyType[0] >= enemyCountMax)
                                {
                                    enemyCountMax = enemyType[0];
                                    secondMost = mostUnits;
                                    mostUnits = 1;
                                }
                            }
                            // Missile/Launcher counter
                            else if (typeCheck.equals(RobotType.MISSILE) || typeCheck.equals(RobotType.LAUNCHER))
                            {
                                enemyType[1]++;
                                if (enemyType[1] >= enemyCountMax)
                                {
                                    enemyCountMax = enemyType[1];
                                    secondMost = mostUnits;
                                    mostUnits = 2;
                                }
                            }
                            // Tank counter
                            else if (typeCheck.equals(RobotType.TANK))
                            {
                                enemyType[2]++;
                                if (enemyType[2] >= enemyCountMax)
                                {
                                    enemyCountMax = enemyType[2];
                                    secondMost = mostUnits;
                                    mostUnits = 3;
                                }
                            }
                            // Basher counter
                            else if (typeCheck.equals(RobotType.BASHER))
                            {
                                enemyType[3]++;
                                if (enemyType[3] >= enemyCountMax)
                                {
                                    enemyCountMax = enemyType[3];
                                    secondMost = mostUnits;
                                    mostUnits = 4;
                                }
                            }
                            // Soldier counter
                            else if (typeCheck.equals(RobotType.SOLDIER))
                            {
                                enemyType[4]++;
                                if (enemyType[4] >= enemyCountMax)
                                {
                                    enemyCountMax = enemyType[4];
                                    secondMost = mostUnits;
                                    mostUnits = 5;
                                }
                            }
                        }
                        // At least one offensive unit attacked the structure
                        if (mostUnits > 0)
                        {
                            secondMost = secondMost << 4;   // Retrieve this with: long secondMost = memoryArray[AttackTiming.ordinal()] >>> 16;
                            mostUnits += secondMost;
                            mostUnits = mostUnits << 12;    // Retrieve this with: long mostUnits = (memoryArray[AttackTiming.ordinal()] >>> 12) & 15;
                            int timing = Clock.getRoundNum();
                            timing += mostUnits;            // Retrieve this with: long timing = memoryArray[AttackTiming.ordinal()] & 4095;
                            rc.setTeamMemory(TeamMemory.AttackTiming.ordinal(), timing);
                        }
                    }
=======
                    rc.broadcast(Messaging.TowerUnderAttack.ordinal(), (i + 1));
>>>>>>> 8d998e5265d9ef5906d5c59afd8c781ff5500e7b
                }
            }
        }
    }

    public void collectData() throws GameActionException
    {
        enemies = rc.senseNearbyRobots(99999, opponent);
        nearByEnemies = rc.senseNearbyRobots(range, opponent);
        allies = rc.senseNearbyRobots(99999, us);
        nearByAllies = rc.senseNearbyRobots(range, us);
    }

    public boolean fight() throws GameActionException
    {
        return fighter.structureFightMicro(nearByEnemies);
    }

}
