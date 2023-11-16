package pl.cyfrowypolsat.cpchromecast.domain.usecase

import com.google.gson.Gson
import pl.cyfrowypolsat.cpchromecast.data.model.send.*
import pl.cyfrowypolsat.cpchromecast.domain.model.media.ChromecastMediaId
import pl.cyfrowypolsat.cpchromecast.domain.model.auth.ChromecastUserAuthData
import javax.inject.Inject

class GetStartPlayerJsonUseCase @Inject constructor() {

    companion object{

    }

    fun getStartPlayerJson(chromecastMediaId: ChromecastMediaId, chromecastUserAuthData: ChromecastUserAuthData, forcePlay: Boolean, portal: String,  startPositionSeconds: Int? = null): String {
        val startPlayer = StartPlayer(
                forcePlay = forcePlay,
                data = getStartPlayerData(chromecastMediaId, chromecastUserAuthData, portal, startPositionSeconds)
        )
        val startPlayerJson = Gson().toJson(startPlayer)
        return startPlayerJson
    }

    private fun getStartPlayerData(chromecastMediaId: ChromecastMediaId, chromecastUserAuthData: ChromecastUserAuthData, portal: String, startPositionSeconds: Int? = null): StartPlayerData {
        return StartPlayerData(
                user = getUser(chromecastUserAuthData),
                media = Media(
                        gmID = GmID(
                                cpid = chromecastMediaId.cpid,
                                id = chromecastMediaId.id
                        )
                ),
                device = chromecastUserAuthData.deviceId,
                clientID = chromecastUserAuthData.clientId ?: "",
                startPosition = startPositionSeconds,
                portal = portal
        )
    }

    private fun getUser(chromecastUserAuthData: ChromecastUserAuthData): User? {
        if (!chromecastUserAuthData.isUserLogged) {
            return null
        }

        return User(
                id = chromecastUserAuthData.userId ?: "",
                session = Session(
                        id = chromecastUserAuthData.sessionId,
                        key = chromecastUserAuthData.sessionKey,
                        keyExpirationTime = chromecastUserAuthData.sessionKeyExpirationTime
                ),
                profileId = chromecastUserAuthData.profileId
        )
    }

}