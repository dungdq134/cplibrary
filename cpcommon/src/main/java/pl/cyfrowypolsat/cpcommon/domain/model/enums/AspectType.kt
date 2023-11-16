package pl.cyfrowypolsat.cpcommon.domain.model.enums

enum class AspectType(val ratio: Float) {
    ASPECT_16x9(16F / 9F),
    ASPECT_7x10(7F / 10F),
    ASPECT_1x1(1F);

    companion object {
        fun get(width: Int, height: Int): AspectType? {
            return if (width == 16 && height == 9) {
                ASPECT_16x9
            } else if (width == 7 && height == 10) {
                ASPECT_7x10
            } else if (width == 1 && height == 1) {
                ASPECT_1x1
            } else {
                null
            }
        }
    }
}