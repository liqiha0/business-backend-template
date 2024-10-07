package io.github.liqiha0.template.gis.utils

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import kotlin.math.*

object CoordinateTransformUtils {

    private const val PI = 3.1415926535897932384626
    private const val A = 6378245.0
    private const val EE = 0.00669342162296594323

    /**
     * GCJ-02 (Tencent, Amap) to WGS-84
     */
    fun gcj02ToWgs84(point: Point): Point {
        if (outOfChina(point.x, point.y)) {
            return point
        }
        val (lon, lat) = gcj02ToWgs84(point.x, point.y)
        val geometryFactory = GeometryFactory(point.precisionModel, 4326)
        return geometryFactory.createPoint(Coordinate(lon, lat))
    }

    /**
     * WGS-84 to GCJ-02 (Tencent, Amap)
     */
    fun wgs84ToGcj02(point: Point): Point {
        if (outOfChina(point.x, point.y)) {
            return point
        }
        val (lon, lat) = wgs84ToGcj02(point.x, point.y)
        val geometryFactory = GeometryFactory(point.precisionModel, 4326)
        return geometryFactory.createPoint(Coordinate(lon, lat))
    }

    private fun gcj02ToWgs84(lng: Double, lat: Double): Pair<Double, Double> {
        val dLat = transformLat(lng - 105.0, lat - 35.0)
        val dLng = transformLon(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        val dLatTransformed = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI)
        val dLngTransformed = (dLng * 180.0) / (A / sqrtMagic * cos(radLat) * PI)
        val mgLat = lat + dLatTransformed
        val mgLng = lng + dLngTransformed
        return Pair(lng * 2 - mgLng, lat * 2 - mgLat)
    }

    private fun wgs84ToGcj02(lng: Double, lat: Double): Pair<Double, Double> {
        val dLat = transformLat(lng - 105.0, lat - 35.0)
        val dLng = transformLon(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        val dLatTransformed = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI)
        val dLngTransformed = (dLng * 180.0) / (A / sqrtMagic * cos(radLat) * PI)
        return Pair(lng + dLngTransformed, lat + dLatTransformed)
    }

    private fun transformLat(lng: Double, lat: Double): Double {
        var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lat * PI) + 40.0 * sin(lat / 3.0 * PI)) * 2.0 / 3.0
        ret += (160.0 * sin(lat / 12.0 * PI) + 320 * sin(lat * PI / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLon(lng: Double, lat: Double): Double {
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lng * PI) + 40.0 * sin(lng / 3.0 * PI)) * 2.0 / 3.0
        ret += (150.0 * sin(lng / 12.0 * PI) + 300.0 * sin(lng / 30.0 * PI)) * 2.0 / 3.0
        return ret
    }

    private fun outOfChina(lng: Double, lat: Double): Boolean {
        return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55)
    }
}
