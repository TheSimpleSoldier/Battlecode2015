package team044;

import battlecode.common.*;

/**
 * Created by David on 1/17/2015.
 */
public class MapDiscovery
{
    public int[][] map;
    public int nwX,nwY,seX,seY;
    public MapDiscovery()
    {
        nwX = 0;
        nwY = 0;
        seX = 0;
        seY = 0;
    }

    private boolean scanner(RobotController rc, int minX, int minY, int maxX, int maxY)
            throws GameActionException
    {
        int check = rc.readBroadcast(Messaging.ScannerChannel.ordinal());
        int mapExtremes = rc.readBroadcast(Messaging.MapExtremes.ordinal());
        int currentX = minX;
        int currentY = minY;
        int xIteration = 0;
        int yIteration = 0;
        if (check != 0)
        {
            minY = rc.readBroadcast(Messaging.ScannerMemoryY2.ordinal());
            minX = rc.readBroadcast(Messaging.ScannerMemoryX2.ordinal());
            currentY = rc.readBroadcast(Messaging.ScannerMemoryY.ordinal());
            currentX = rc.readBroadcast(Messaging.ScannerMemoryX.ordinal());
            rc.broadcast(Messaging.ScannerChannel.ordinal(), 0);
            yIteration = currentY - minY;
            xIteration = currentX - minX;
        }
        else if (mapExtremes != 0)
        {
            minX = rc.readBroadcast(Messaging.MapLimitWest.ordinal());
            minY = rc.readBroadcast(Messaging.MapLimitNorth.ordinal());
            maxX = rc.readBroadcast(Messaging.MapLimitEast.ordinal());
            maxY = rc.readBroadcast(Messaging.MapLimitSouth.ordinal());
        }
        int xLimit = maxX - minX;
        int yLimit = maxY - minY;
        if (map == null || (check == 0 && xLimit > map[0].length && yLimit > map.length))
        {
            map = new int[yLimit][xLimit];
            nwX = minX;
            nwY = minY;
            seX = maxX;
            seY = maxY;
            rc.broadcast(Messaging.CurrentOriginX.ordinal(), minX);
            rc.broadcast(Messaging.CurrentOriginY.ordinal(), minY);
        }
        else if (check == 0 && xLimit > map[0].length)
        {
            map = new int[map.length][xLimit];
            nwX = minX;
            seX = maxX;
            rc.broadcast(Messaging.CurrentOriginX.ordinal(), minX);
        }
        else if (check == 0 && yLimit > map.length)
        {
            map = new int[yLimit][map[0].length];
            nwY = minY;
            seY = maxY;
            rc.broadcast(Messaging.CurrentOriginY.ordinal(), minY);
        }
        MapLocation point = new MapLocation(minX,minY);
        int fog1 = 0;
        int fog2 = 0;
        int fog3 = 0;
        int fog4 = 0;
        MapLocation nextPoint = new MapLocation(currentX, currentY);
        //System.out.println(xLimit + " " + yLimit + " ");
        for (int i = yIteration; i < yLimit; i++)
        {
            //System.out.println("yIteration:  " + Clock.getBytecodesLeft());
            for (int j = xIteration; j < xLimit; j++)
            {
                //System.out.println("xIteration:  " + Clock.getBytecodesLeft());
                int byteCodesLeft = Clock.getBytecodesLeft();
                if (byteCodesLeft < 400)
                {
                    rc.broadcast(Messaging.ScannerMemoryX.ordinal(), nextPoint.x);
                    rc.broadcast(Messaging.ScannerMemoryY.ordinal(), nextPoint.y);
                    rc.broadcast(Messaging.ScannerMemoryY2.ordinal(), point.y);
                    rc.broadcast(Messaging.ScannerMemoryX2.ordinal(), point.x);
                    rc.broadcast(Messaging.ScannerChannel.ordinal(), 1);
                    return true;
                }
                if (map[i][j] == 0)
                {
                    TerrainTile nextTile = rc.senseTerrainTile(nextPoint);
                    if (nextTile.equals(TerrainTile.NORMAL))
                    {
                        map[i][j] = 1;
                    } else if (nextTile.equals(TerrainTile.VOID))
                    {
                        map[i][j] = 2;
                    } else if (nextTile.equals(TerrainTile.OFF_MAP))
                    {
                        map[i][j] = 3;
                    }
                    else if (mapExtremes != 0)
                    {
                        if (j < xLimit/2 && i < yLimit/2)
                            fog1++;
                        else if (j > xLimit/2 && i < yLimit/2)
                            fog2++;
                        else if (j < xLimit/2)
                            fog2++;
                        else if (j > xLimit/2)
                            fog2++;
                    }
                }
                nextPoint = nextPoint.add(Direction.EAST);
                //System.out.print(map[i][j]);
            }
            xIteration = 0;
            point = point.add(Direction.SOUTH);
            nextPoint = new MapLocation(point.x,point.y);
            //System.out.println();
        }
        //System.out.print("Exit:  " + Clock.getBytecodesLeft());
        if (mapExtremes != 0)
        {
            MapLocation enemyHQ = rc.senseEnemyHQLocation();
            int broadcast = 0;
            if (enemyHQ.x - minX < xLimit/2 && enemyHQ.y < yLimit/2)
                broadcast = Math.max(Math.max(fog2,fog3),fog4);
            else if (enemyHQ.x - minX < xLimit/2)
                broadcast = Math.max(Math.max(fog1,fog2),fog4);
            else if (enemyHQ.x - minX > xLimit/2 && enemyHQ.y < yLimit/2)
                broadcast = Math.max(Math.max(fog1,fog3),fog4);
            else
                broadcast = Math.max(Math.max(fog1,fog2),fog3);
            rc.broadcast(Messaging.MapExtremes.ordinal(), broadcast);
        }
        rc.broadcast(Messaging.ScannerChannel.ordinal(), 0);
        rc.broadcast(Messaging.ScannerMemoryY.ordinal(), 0);
        rc.broadcast(Messaging.ScannerMemoryX.ordinal(), 0);
        rc.broadcast(Messaging.ScannerMemoryX2.ordinal(), 0);
        rc.broadcast(Messaging.ScannerMemoryY2.ordinal(), 0);
        //System.out.println("  " + Clock.getBytecodesLeft());
        //System.out.println();
        return true;
    }

    private void findMap(RobotController rc) throws GameActionException
    {
        MapLocation hq = rc.senseHQLocation();
        RobotInfo[] friends = rc.senseNearbyRobots(9999999, rc.getTeam());

        MapLocation minX, minY, maxX, maxY;
        minX = hq;
        minY = hq;
        maxX = hq;
        maxY = hq;
        for (int i = 0; i < friends.length; i++)
        {
            if (friends[i].location.x > maxX.x)
            {
                maxX = friends[i].location;
            }
            else if (friends[i].location.x < minX.x)
            {
                minX = friends[i].location;
            }
            if (friends[i].location.y > maxY.y)
            {
                maxY = friends[i].location;
            }
            else if (friends[i].location.y < minY.y)
            {
                minY = friends[i].location;
            }
        }

        int x1 = rc.readBroadcast(Messaging.MapLimitWest.ordinal());
        if (x1 == 0)
        {
            x1 = minX.x-3;
        }

        int x2 = rc.readBroadcast(Messaging.MapLimitEast.ordinal());
        if (x2 == 0)
        {
            x2 = maxX.x+3;
        }

        int y1 = rc.readBroadcast(Messaging.MapLimitNorth.ordinal());
        if (y1 == 0)
        {
            y1 = minY.y-3;
        }

        int y2 = rc.readBroadcast(Messaging.MapLimitSouth.ordinal());
        if (y2 == 0)
        {
            y2 = maxY.y+3;
        }
        scanner(rc, x1, y1, x2, y2);
    }

    private boolean findEdges(RobotController rc) throws GameActionException
    {
        if (rc.readBroadcast(Messaging.MapExtremes.ordinal()) != 0)
        {
            return true;
        }
        MapLocation hq = rc.senseHQLocation();
        RobotInfo[] friends = rc.senseNearbyRobots(9999999, rc.getTeam());

        MapLocation minX, minY, maxX, maxY;
        minX = hq;
        minY = hq;
        maxX = hq;
        maxY = hq;
        for (int i = 0; i < friends.length; i++)
        {
            if (friends[i].location.x > maxX.x)
            {
                maxX = friends[i].location;
            }
            else if (friends[i].location.x < minX.x)
            {
                minX = friends[i].location;
            }
            if (friends[i].location.y > maxY.y)
            {
                maxY = friends[i].location;
            }
            else if (friends[i].location.y < minY.y)
            {
                minY = friends[i].location;
            }
        }
        boolean x1 = true;
        boolean x2 = true;
        boolean y1 = true;
        boolean y2 = true;
        while (x1 || x2 || y1 || y2)
        {
            minY = minY.add(Direction.NORTH);
            minX = minX.add(Direction.WEST);
            maxY = maxY.add(Direction.SOUTH);
            maxX = maxX.add(Direction.EAST);
            x1 = rc.canSenseLocation(minX);
            x2 = rc.canSenseLocation(maxX);
            y1 = rc.canSenseLocation(minY);
            y2 = rc.canSenseLocation(maxY);
            if (x2 && rc.senseTerrainTile(maxX).equals(TerrainTile.OFF_MAP))
            {
                maxX = maxX.add(Direction.WEST);
                rc.broadcast(Messaging.MapLimitEast.ordinal(),maxX.x);
                x2 = false;
                rc.broadcast(Messaging.DroneMaxX.ordinal(),0);
            }
            if (y2 && rc.senseTerrainTile(maxY).equals(TerrainTile.OFF_MAP))
            {
                maxY = maxY.add(Direction.NORTH);
                rc.broadcast(Messaging.MapLimitSouth.ordinal(),maxY.y);
                y2 = false;
                rc.broadcast(Messaging.DroneMaxY.ordinal(),0);
            }
            if (x1 && rc.senseTerrainTile(minX).equals(TerrainTile.OFF_MAP))
            {
                minX = minX.add(Direction.EAST);
                rc.broadcast(Messaging.MapLimitWest.ordinal(),minX.x);
                x1 = false;
                rc.broadcast(Messaging.DroneMinX.ordinal(), 0);
            }
            if (y1 && rc.senseTerrainTile(minY).equals(TerrainTile.OFF_MAP))
            {
                minY = minY.add(Direction.SOUTH);
                rc.broadcast(Messaging.MapLimitNorth.ordinal(),minY.y);
                y1 = false;
                rc.broadcast(Messaging.DroneMinY.ordinal(), 0);
            }
        }
        return false;
    }
    private boolean canScan(RobotController rc, int n, int s, int e, int w) throws
            GameActionException
    {
        int knownEdges = 0;
        if (n != 0)
            knownEdges++;
        else
            rc.broadcast(Messaging.DroneMinY.ordinal(), 1);
        if (s != 0)
            knownEdges++;
        else
            rc.broadcast(Messaging.DroneMaxY.ordinal(),1);
        if (e != 0)
            knownEdges++;
        else
            rc.broadcast(Messaging.DroneMaxX.ordinal(),1);
        if (w != 0)
            knownEdges++;
        else
            rc.broadcast(Messaging.DroneMinX.ordinal(),1);
        if (knownEdges == 4)
        {
            rc.broadcast(Messaging.MapExtremes.ordinal(), 5);
            return true;
        }
        else
        {
            return false;
        }
    }
    public int[][] checkMap(RobotController rc) throws GameActionException
    {
        int bytecodes = Clock.getBytecodesLeft();
        if (bytecodes > 1500) {
            int minX = rc.readBroadcast(Messaging.MapLimitWest.ordinal());
            int minY = rc.readBroadcast(Messaging.MapLimitNorth.ordinal());
            int maxX = rc.readBroadcast(Messaging.MapLimitEast.ordinal());
            int maxY = rc.readBroadcast(Messaging.MapLimitSouth.ordinal());
            if (canScan(rc, minY, maxY, maxX, minX)) {
                scanner(rc, minX, minY, maxX, maxY);
            } else if (findEdges(rc)) {
                scanner(rc, minX, minY, maxX, maxY);
            } else {
                findMap(rc);
            }
        }
        return map;
    }
}
