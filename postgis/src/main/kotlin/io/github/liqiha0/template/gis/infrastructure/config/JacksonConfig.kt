package io.github.liqiha0.template.gis.infrastructure.config

import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.n52.jackson.datatype.jts.JtsModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun jtsModule(): JtsModule {
        val geometryFactory = GeometryFactory(PrecisionModel(), 4326)
        val module = JtsModule(geometryFactory)
        return module
    }
}