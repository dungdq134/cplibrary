package pl.cyfrowypolsat.cpplayerexternal.domain

import pl.cyfrowypolsat.cpplayerexternal.data.model.Ads
import pl.cyfrowypolsat.cpplayerexternal.data.model.ExtraData


class BuildAdsUrlUseCase {

    fun buildAdsUrl(ads: Ads?): String? {
        val videoPlacement = ads?.placements?.find { it.position == "video" }
        val plainUrl = videoPlacement?.let { ads.adserver + videoPlacement.id + buildTags(ads.tags) + buildStaticData() + buildExtraData(ads.extraData) }
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

}