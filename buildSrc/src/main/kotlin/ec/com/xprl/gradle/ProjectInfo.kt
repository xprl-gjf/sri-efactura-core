package ec.com.xprl.gradle

import kotlinx.serialization.*

@Serializable
data class ProjectInfo (
    val group: String,
    val version: String,
    val projectUrl: String? = null,
    var developers: Array<DeveloperInfo> = emptyArray(),
    var license: LicenseInfo? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProjectInfo

        if (group != other.group) return false
        if (version != other.version) return false
        if (projectUrl != other.projectUrl) return false
        if (license != other.license) return false
        if (!developers.contentEquals(other.developers)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = group.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + projectUrl.hashCode()
        result = 31 * result + developers.contentHashCode()
        result = 31 * result + license.hashCode()
        return result
    }
}

@Serializable
data class DeveloperInfo(
    val id: String,
    val name: String,
    val email: String? = null,
    val organization: String? = null
)

@Serializable
data class LicenseInfo(
    val name: String,
    val url: String? = null
)
