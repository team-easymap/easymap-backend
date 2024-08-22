package com.easymap.easymap.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PoiUtil {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point createPointFromCoordinates(double latitude, double longitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude); // 경도(longitude)가 먼저, 위도(latitude)가 나중에
        return geometryFactory.createPoint(coordinate);
    }
}
