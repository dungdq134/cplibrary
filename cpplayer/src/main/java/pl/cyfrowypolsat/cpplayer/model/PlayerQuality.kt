package pl.cyfrowypolsat.cpplayer.model


enum class PlayerQuality(val maxHeight: Int) {
    LOW(719),
    MEDIUM(1079),
    HIGH(1439),
    AUTO(Int.MAX_VALUE);

    companion object {
        fun from(height: Int): PlayerQuality {
            return when (height) {
                in Int.MIN_VALUE..LOW.maxHeight -> {
                    LOW
                }
                in LOW.maxHeight..MEDIUM.maxHeight -> {
                    MEDIUM
                }
                in MEDIUM.maxHeight..HIGH.maxHeight -> {
                    HIGH
                }
                else -> {
                    AUTO
                }
            }
        }
    }
}