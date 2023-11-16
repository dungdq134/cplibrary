package pl.cyfrowypolsat.cpplayer.model

import androidx.media3.exoplayer.offline.DownloadRequest
import pl.cyfrowypolsat.cpdata.api.common.enums.Cpid
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig

data class PlayerData(val playerConfig: PlayerConfig,
                      val successor: MediaSuccessor?,
                      val images: List<PlayerImageSource>,
                      var downloadRequest: DownloadRequest? = null)

data class MediaSuccessor(val id: String,
                          val cpid: Int)

fun PlayerData.toDownloadData(): DownloadData {
    return DownloadData(id = playerConfig.id,
            cpid = Cpid.VOD.type,
            url = playerConfig.url,
            mediaType = playerConfig.mediaType,
            title = playerConfig.title,
            ageGroup = playerConfig.ageGroup,
            userAgent = playerConfig.userAgent,
            images = images
    )
}