package pl.cyfrowypolsat.cpdata.api.common.enums

enum class Cpid(val type: Int) {
    CATEGORY(7),
    PACKET(9),
    LIVE(0),
    CHANNEL_PROGRAM(12),
    VOD(1);

    companion object {
        fun getFromInt(type: Int): Cpid {
            return when (type) {
                CATEGORY.type -> CATEGORY
                PACKET.type -> PACKET
                VOD.type -> VOD
                LIVE.type -> LIVE
                CHANNEL_PROGRAM.type -> CHANNEL_PROGRAM
                else -> VOD
            }
        }
    }
}