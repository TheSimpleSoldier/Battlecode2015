package team044.Units;


import battlecode.common.*;
import team044.*;

public class Missile
{
    public static void run(RobotController rc)
    {
        rc.setIndicatorString(2, "Ready to run: " + Clock.getBytecodeNum() + ", " + Clock.getBytecodeNum());

        RobotInfo[] nearByEnemies;
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        Direction dir;
        MapLocation target = null;
        boolean foundLauncher = false;
        MapLocation us;

        while (true)
        {
            try
            {
                if (!rc.isCoreReady())
                {
                    rc.setIndicatorString(1, "!rc.isCoreReady()");
                    continue;
                }

                nearByEnemies = rc.senseNearbyRobots(24, rc.getTeam().opponent());

                if (nearByEnemies.length == 0)
                {
                    rc.setIndicatorString(0, "nearByEnemies: " + Clock.getBytecodeNum() + ", round: " + Clock.getRoundNum());

                    int x = rc.readBroadcast(7893);
                    int y = rc.readBroadcast(7894);

                    MapLocation closest = new MapLocation(x,y);
                    us = rc.getLocation();


                    dir = us.directionTo(closest);

                    int byteCodes = Clock.getBytecodeNum();

                    if (rc.canMove(dir))
                    {
                        rc.move(dir);
                    }
                    else if (rc.canMove(dir.rotateRight()))
                    {
                        rc.move(dir.rotateRight());
                    }
                    else if (rc.canMove(dir.rotateLeft()))
                    {
                        rc.move(dir.rotateLeft());
                    }
                    else if (rc.canMove(dir.rotateLeft().rotateLeft()))
                    {
                        rc.move(dir.rotateLeft().rotateLeft());
                    }
                    else if (rc.canMove(dir.rotateRight().rotateRight()))
                    {
                        rc.move(dir.rotateRight().rotateRight());
                    }
                    rc.setIndicatorString(1, "After movement: " + Clock.getBytecodeNum() + ", round: " + Clock.getRoundNum() + ", before: " + byteCodes);
                }
                else
                {
                    rc.setIndicatorString(0, "Inside of else: " + Clock.getBytecodeNum() + ", round: " + Clock.getRoundNum());
                    for (int i = nearByEnemies.length; --i>=0;)
                    {
                        if (nearByEnemies[i].type == RobotType.LAUNCHER)
                        {
                            dir = rc.getLocation().directionTo(nearByEnemies[i].location);
                            foundLauncher = true;
                            if (rc.canMove(dir))
                            {
                                rc.move(dir);
                                continue;
                            }

                            if (rc.canMove(dir.rotateRight()))
                            {
                                rc.move(dir.rotateRight());
                                continue;
                            }

                            if (rc.canMove(dir.rotateLeft()))
                            {
                                rc.move(dir.rotateLeft());
                                continue;
                            }

                            if (rc.canMove(dir.rotateLeft().rotateLeft()))
                            {
                                rc.move(dir.rotateLeft().rotateLeft());
                                continue;
                            }

                            if (rc.canMove(dir.rotateRight().rotateRight()))
                            {
                                rc.move(dir.rotateRight().rotateRight());
                                continue;
                            }
                        }
                        else if (nearByEnemies[i].type != RobotType.MISSILE)
                        {
                            target = nearByEnemies[i].location;
                        }
                    }

                    if (!foundLauncher)
                    {
                        dir = rc.getLocation().directionTo(target);
                        foundLauncher = false;
                        if (rc.canMove(dir))
                        {
                            rc.move(dir);
                        }

                        else if (rc.canMove(dir.rotateRight()))
                        {
                            rc.move(dir.rotateRight());
                        }

                        else if (rc.canMove(dir.rotateLeft()))
                        {
                            rc.move(dir.rotateLeft());
                        }

                        else if (rc.canMove(dir.rotateLeft().rotateLeft()))
                        {
                            rc.move(dir.rotateLeft().rotateLeft());
                        }

                        else if (rc.canMove(dir.rotateRight().rotateRight()))
                        {
                            rc.move(dir.rotateRight().rotateRight());
                        }
                        rc.setIndicatorString(1, "!foundLauncher: " + Clock.getBytecodeNum() + ", round: " + Clock.getRoundNum());
                    }


                }

                rc.yield();
            }
            catch (Exception e)
            {
                rc.yield();
            }
        }
    }
}
