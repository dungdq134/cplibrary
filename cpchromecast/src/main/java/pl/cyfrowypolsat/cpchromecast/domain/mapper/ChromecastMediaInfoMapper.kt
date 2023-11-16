package pl.cyfrowypolsat.cpchromecast.domain.mapper

import pl.cyfrowypolsat.cpchromecast.data.model.received.PlaybackReady
import pl.cyfrowypolsat.cpchromecast.domain.model.media.ChromecastMediaInfo
import javax.inject.Inject

class ChromecastMediaInfoMapper @Inject constructor() {

    fun map(playbackReady: PlaybackReady): ChromecastMediaInfo? {

        val source = playbackReady.data?.media?.sources?.getOrNull(0) ?: return null

        return if (source.mediaId.isNullOrBlank()) {
            return null
        } else ChromecastMediaInfo(
                source.mediaId,
                source.cpid,
                playbackReady.data.media.title)
    }
}