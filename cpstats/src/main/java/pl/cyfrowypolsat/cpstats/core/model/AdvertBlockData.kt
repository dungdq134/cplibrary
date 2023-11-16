package pl.cyfrowypolsat.cpstats.core.model

data class AdvertBlockData(
        val blockType: AdvertBlockType,
        val totalAdsCount: Int,
        val blockIndex: Int,
        val timeOffsetSeconds: Double)