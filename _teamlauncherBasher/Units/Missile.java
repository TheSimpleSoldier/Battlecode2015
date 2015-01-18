package _teamlauncherBasher.Units;


import battlecode.common.*;
import _teamlauncherBasher.*;

public class Missile
{
    public static void run(RobotController rc)
    {
        rc.setIndicatorString(2, "Ready to run: " + Clock.getBytecodeNum() + ", " + Clock.getBytecodeNum());

        RobotInfo[] nearByEnemies;
        MapLocation enemyHQ = rc.senseEnemyHQLocation();
        Direction dir = null;
        MapLocation target = null;
        boolean foundLauncher = false;
        MapLocation us;

        while (true)
        {
            try
            {
                if (!rc.isCoreReady())
                {
                    continue;
                }

                target = null;
                nearByEnemies = rc.senseNearbyRobots(24, rc.getTeam().opponent());

                if (nearByEnemies.length == 0)
                {
                    int x = rc.readBroadcast(7893);
                    int y = rc.readBroadcast(7894);

                    MapLocation closest = new MapLocation(x,y);
                    us = rc.getLocation();

                    if (us.distanceSquaredTo(closest) < 64)
                    {
                        dir = us.directionTo(closest);

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
                    }
                    else
                    {
                        RobotInfo[] nearByAllies = rc.senseNearbyRobots(15, rc.getTeam());

                        for (int i = nearByAllies.length; --i>=0; )
                        {
                            if (nearByAllies[i].type == RobotType.LAUNCHER)
                            {
                                dir = rc.getLocation().directionTo(nearByAllies[i].location).opposite();
                                break;
                            }
                            else if (dir != null && nearByAllies[i].type != RobotType.MISSILE)
                            {
                                dir = rc.getLocation().directionTo(nearByAllies[i].location).opposite();
                            }
                        }

                        if (dir == null)
                        {
                            // don't see any enemies and aren't next to an ally
                        }
                        else if (rc.canMove(dir))
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
                    }
                }
                else
                {
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
                        if (target == null)
                        {
                            RobotInfo[] nearByAllies = rc.senseNearbyRobots(15, rc.getTeam());

                            for (int i = nearByAllies.length; --i>=0; )
                            {
                                if (nearByAllies[i].type == RobotType.LAUNCHER)
                                {
                                    dir = rc.getLocation().directionTo(nearByAllies[i].location).opposite();
                                    break;
                                }
                                else if (dir != null && nearByAllies[i].type != RobotType.MISSILE)
                                {
                                    dir = rc.getLocation().directionTo(nearByAllies[i].location).opposite();
                                }
                            }

                            if (dir == null)
                            {
                                // don't see any enemies and aren't next to an ally
                            }
                            else if (rc.canMove(dir))
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
                        }
                        else
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
                        }
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
