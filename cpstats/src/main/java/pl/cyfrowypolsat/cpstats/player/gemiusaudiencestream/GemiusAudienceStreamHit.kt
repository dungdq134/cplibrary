package pl.cyfrowypolsat.cpstats.player.gemiusaudiencestream

import com.gemius.sdk.stream.*

const val TRANSMISSION_TYPE_BROADCAST = 2
const val TRANSMISSION_TYPE_ON_DEMAND = 1

interface GemiusAudienceStreamHit {
    data class NewProgramHit(val programId: String,
                             val programData: ProgramData) : GemiusAudienceStreamHit

    data class ProgramEventHit(val programId: String,
                               val offset: Int,
                               val eventType: Player.EventType,
                               val eventProgramData: EventProgramData) : GemiusAudienceStreamHit

    data class NewAdHit(val adId: String,
                        val adData: AdData) : GemiusAudienceStreamHit

    data class AdEventHit(val programId: String,
                          val adId: String,
                          val offset: Int,
                          val eventType: Player.EventType,
                          val eventAdData: EventAdData) : GemiusAudienceStreamHit
}