package pl.cyfrowypolsat.cpchromecast.domain.model.media

data class ChromecastMediaInfo constructor(val id: String,
                                           val cpid: Int,
                                           val title: String?) {

    fun toChromecastMediaId(): ChromecastMediaId {
        return ChromecastMediaId(id, cpid)
    }
}