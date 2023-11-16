package pl.cyfrowypolsat.cpplayer

import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.Ads
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.ExtraData


class BuildAdsUrlUseCase {

    fun buildAdsUrl(ads: Ads?,
                    seenPercent: Int?): String? {
        val videoPlacement = ads?.placements?.find { it.position == "video" }
        val plainUrl =  videoPlacement?.let { ads.adserver + videoPlacement.id + buildTags(ads.tags) + buildStaticData() + buildExtraData(ads.extraData) + buildSeenPercentData(seenPercent) }
        return plainUrl?.replace(" ", "%20")
    }

    private fun buildTags(tags: List<String>?): String {
        return if (tags != null && tags.isNotEmpty()) {
            "/key=" + tags.joinToString(",")
        } else {
            ""
        }
    }

    private fun buildStaticData(): String {
        val builder = StringBuilder()
        builder.append("/aocodetype=1")
        builder.append("/fmajor=0")
        builder.append("/fminor=0")
        return builder.toString()
    }

    private fun buildExtraData(extraData: List<ExtraData>?): String {
        val builder = StringBuilder()
        extraData?.forEach { builder.append("/${it.name}=${it.value}") }
        return builder.toString()
    }

    private fun buildSeenPercentData(seenPercent: Int?): String {
        val builder = StringBuilder()
        seenPercent?.let {
            builder.append("/plb_offset=")
            builder.append(seenPercent)
        }

        return builder.toString()
    }
}