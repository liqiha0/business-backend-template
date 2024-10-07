plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "backend-template"

include("app")
include("core")
include("storage")
include("order")
include("verification")
include("notification")
include("gis")