package pl.cyfrowypolsat.cpplayercore.configuration

data class OverlayInfo(val type: OverlayType,
                       val enabled: Boolean = false,
                       val autostart: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        return try {
            if (this === other) return true
            if (other == null) return false
            if (javaClass != other.javaClass) return false
            val that = other as OverlayInfo
            this.type == that.type
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}

enum class OverlayType(val type: String) {
    TERAVOLT("teravolt");
}