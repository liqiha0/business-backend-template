package io.github.liqiha0.template.core

import org.springframework.beans.factory.InitializingBean
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DatabaseInitializationRunner(
    private val initializers: List<DatabaseInitializer>,
    private val dataSource: DataSource
) : InitializingBean, Ordered {

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
    override fun afterPropertiesSet() {
        initializers.forEach { initializer ->
            initializer.getFlyway(this.dataSource).migrate()
        }
    }
}
