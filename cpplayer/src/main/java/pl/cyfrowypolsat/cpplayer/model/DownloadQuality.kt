package pl.cyfrowypolsat.cpplayer.model

enum class DownloadQuality(val maxHeight: Int) {
    STANDARD(700),
    HIGH(Int.MAX_VALUE);

    companion object {
        fun from(height: Int): DownloadQuality {
            if (height <= STANDARD.maxHeight) {
                return STANDARD
            } else {
                return HIGH
            }
        }
    }
}