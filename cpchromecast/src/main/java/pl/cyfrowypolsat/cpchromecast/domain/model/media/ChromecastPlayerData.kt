package pl.cyfrowypolsat.cpchromecast.domain.model.media

import pl.cyfrowypolsat.cpchromecast.domain.model.auth.ChromecastUserAuthData

data class ChromecastPlayerData(val chromecastMediaId: ChromecastMediaId,
                                val chromecastUserAuthData: ChromecastUserAuthData,
                                val forcePlay: Boolean,
                                val startPositionSeconds: Int?,
                                val portal: String)