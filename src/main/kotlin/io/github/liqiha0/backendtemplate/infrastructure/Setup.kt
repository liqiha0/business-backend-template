package io.github.liqiha0.backendtemplate.infrastructure

import io.github.liqiha0.backendtemplate.application.system.DefaultAdminAccountInitializer
import io.github.liqiha0.backendtemplate.application.system.DefaultRoleInitializer
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Setup(
    val defaultAdminAccountInitializer: DefaultAdminAccountInitializer,
    val defaultRoleInitializer: DefaultRoleInitializer
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        this.defaultAdminAccountInitializer.createDefaultAdminAccount()
        this.defaultRoleInitializer.createDefaultAdminRole()
    }
}