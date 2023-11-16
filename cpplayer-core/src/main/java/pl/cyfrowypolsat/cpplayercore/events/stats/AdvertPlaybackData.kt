package pl.cyfrowypolsat.cpplayercore.events.stats

data class AdvertPlaybackData(val advertId: String,
                              val advertIndex: Int,
                              val advertTitle: String,
                              val advertDurationSeconds: Long,
                              val blockTotalAdsCount: Int,
                              val blockIndex: Int,
                              val blockTimeOffsetSeconds: Double)