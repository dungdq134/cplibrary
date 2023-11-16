package pl.cyfrowypolsat.cpdata.api.concurrentaccess.request

import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId

data class PlaybackStart(val system: String = "cac",
                         val action: String = "playbackStart",
                         val deviceInfo: DeviceInfo,
                         val playbackData: PlaybackStartData,
                         val jwtToken: String)

data class DeviceInfo(val deviceId: DeviceId,
                      val deviceName: String)

data class PlaybackStartData(val licenseId: String,
                             val mediaId: PlaybackMediaId)

data class PlaybackMediaId(val cpid: Int,
                           val id: String)


