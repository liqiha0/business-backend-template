package io.github.liqiha0.template.gis.infrastructure.config

import io.swagger.v3.oas.annotations.media.Schema
import org.locationtech.jts.geom.Point
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Configuration

@Configuration
class GisSpringDocConfig {
    init {
        SpringDocUtils.getConfig().replaceWithClass(Point::class.java, GeoJsonPoint::class.java)
    }
}

@Schema(name = "GeoJsonPoint", description = "GeoJSON Point 格式的地理坐标点")
data class GeoJsonPoint(
    @Schema(description = "类型，固定为 'Point'", required = true, example = "Point")
    val type: String = "Point",

    @Schema(description = "坐标数组 [经度, 纬度]", required = true, example = "[116.404, 39.915]")
    val coordinates: List<Double>
)
