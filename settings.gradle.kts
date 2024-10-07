plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "backend-template"

include("starter")
include("core")
include("storage")
include("order")
include("verification")
include("notification")
include("postgis")
include("app")
