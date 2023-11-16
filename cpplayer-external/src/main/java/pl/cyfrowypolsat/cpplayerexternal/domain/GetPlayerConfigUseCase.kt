package pl.cyfrowypolsat.cpplayerexternal.domain

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayerexternal.data.RetrofitClient

class GetPlayerConfigUseCase {

    fun getPlayerConfig(url: String): Observable<PlayerConfig> {
        val mapper = PlayerConfigMapper()
        return RetrofitClient.apiInterface.getPrePlayData(url)
                .map {
                    val adsUrl = BuildAdsUrlUseCase().buildAdsUrl(ads = it.ads)
                    mapper.map(it, adsUrl)
                }
    }

}