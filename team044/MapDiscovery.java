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
        if (mapExtremes != 0)
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
            yLimit = map.length;
            map = new int[yLimit][xLimit];
            nwX = minX;
            seX = maxX;
            rc.broadcast(Messaging.CurrentOriginX.ordinal(), minX);
        }
        else if (check == 0 && yLimit > map.length)
        {
            xLimit = map[0].length;
            map = new int[yLimit][xLimit];
            nwY = minY;
            seY = maxY;
            rc.broadcast(Messaging.CurrentOriginY.ordinal(), minY);
        }
        else
        {
            xLimit = map[0].length;
            yLimit = map.length;
        }
        rc.broadcast(Messaging.MapSize.ordinal(), xLimit*yLimit);
        MapLocation point = new MapLocation(minX,minY);
        int[][] fog = new int[2][2];
        int oreSpot = 0;
        int oreSpotX = 0;
        int oreSpotY = 0;
        int oreSpotX2 = 0;
        int oreSpotY2 = 0;
        int[] sectorX = new int[3];
        int[] sectorY = new int[3];
        sectorX[0] = xLimit/4;
        sectorY[0] = yLimit/4;
        sectorX[1] = xLimit/2 + 1;
        sectorY[1] = yLimit/2 + 1;
        sectorX[2] = xLimit/2 + sectorX[0];
        sectorY[2] = yLimit/2 + sectorY[0];
        MapLocation nextPoint = new MapLocation(currentX, currentY);
//        System.out.println(xLimit + " " + yLimit + " ");
        for (int i = yIteration; i < yLimit; i++)
        {
//            System.out.println("yIteration:  " + Clock.getBytecodesLeft());
            for (int j = xIteration; j < xLimit; j++)
            {
//                System.out.println("xIteration:  " + Clock.getBytecodesLeft());
                int byteCodesLeft = Clock.getBytecodesLeft();
                if (byteCodesLeft < 300)
                {
                    rc.yield();
                }
                if (map[i][j] == 0)
                {
                    TerrainTile nextTile = rc.senseTerrainTile(nextPoint);
                    switch (nextTile)
                    {
                        case NORMAL:
                            map[i][j] = 1;
                            break;
                        case VOID:
                            map[i][j] = 2;
                            break;
                        case OFF_MAP:
                            map[i][j] = 3;
                            break;
                        default:
                            if (mapExtremes != 0)
                            {
                                int quadX = j / sectorX[1];
                                int quadY = i / sectorY[1];
                                fog[quadY][quadX]++;
                            }
                            break;
                    }
                }
                if (rc.canSenseLocation(nextPoint))
                {
                    int spot = (int) rc.senseOre(nextPoint);
                    if (spot > oreSpot)
                    {
                        oreSpotX = nextPoint.x;
                        oreSpotY = nextPoint.y;
                        oreSpot = spot;
                    }
                    else if (spot == oreSpot)
                    {
                        oreSpotX2 = nextPoint.x;
                        oreSpotY2 = nextPoint.y;
                    }
                }
                nextPoint = nextPoint.add(Direction.EAST);
//                System.out.print(map[i][j]);
            }
            xIteration = 0;
            point = point.add(Direction.SOUTH);
            nextPoint = new MapLocation(point.x,point.y);
//            System.out.println();
        }
//        System.out.print("Exit:  " + Clock.getBytecodesLeft());
        if (mapExtremes != 0)
        {
            MapLocation enemyHQ = rc.senseEnemyHQLocation();
            int broadcastFog = 0;
            if (enemyHQ.x - minX < sectorX[1] && enemyHQ.y - minY < sectorY[1]) {
                broadcastFog = Math.max(Math.max(fog[0][1], fog[1][0]), fog[1][1]);
            } else if (enemyHQ.x - minX < sectorX[1]) {
                broadcastFog = Math.max(Math.max(fog[0][0], fog[1][0]), fog[1][1]);
            } else if (enemyHQ.x - minX > sectorX[1] && enemyHQ.y < sectorY[1]) {
                broadcastFog = Math.max(Math.max(fog[0][0], fog[0][1]), fog[1][1]);
            } else {
                broadcastFog = Math.max(Math.max(fog[0][0], fog[0][1]), fog[1][0]);
            }
            if (broadcastFog == fog[0][0])
                broadcastFog = 1;
            else if (broadcastFog == fog[0][1])
                broadcastFog = 2;
            else if (broadcastFog == fog[1][0])
                broadcastFog = 3;
            else
                broadcastFog = 4;
            rc.broadcast(Messaging.MapExtremes.ordinal(), broadcastFog);
        }
        int oreX = rc.readBroadcast(Messaging.OreX.ordinal());
        int oreY = rc.readBroadcast(Messaging.OreY.ordinal());
        if (oreX == oreSpotX || oreY == oreSpotY)
            return true;
        rc.broadcast(Messaging.BestOre.ordinal(),oreSpot);
        rc.broadcast(Messaging.OreX.ordinal(),oreSpotX);
        rc.broadcast(Messaging.OreY.ordinal(),oreSpotY);
        rc.broadcast(Messaging.BestSpotMiners.ordinal(),0);
        rc.broadcast(Messaging.OreX2.ordinal(),oreSpotX2);
        rc.broadcast(Messaging.OreY2.ordinal(),oreSpotY2);
        rc.broadcast(Messaging.BestSpot2Miners.ordinal(),0);
//        System.out.println("  " + Clock.getBytecodesLeft());
//        System.out.println();
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
    public int[][] checkMap(RobotController rc) throws GameActionException {
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
        return map;
    }
}
