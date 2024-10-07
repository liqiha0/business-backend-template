package io.github.liqiha0.backendtemplate.domain.model.system

interface Authority {
    val key: String
    val displayName: String
    val parent: Authority?
}

object Administrator : Authority {
    override val key: String = "ADMINISTRATOR"
    override val displayName: String = "Administrator"
    override val parent: Authority? = null
}