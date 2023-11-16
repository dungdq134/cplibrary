package pl.cyfrowypolsat.cpstats.player.gemiusprism

import pl.cyfrowypolsat.cpstats.core.gemiusprism.GemiusPrismUtils

data class GemiusPrismHit constructor(val id: String,
                                      val hrefParamsUrl: String,
                                      val extraParamsUrl: String,
                                      val eventType: Type) {

    enum class Type constructor(val type: String) {
        ACTION("action"),
    }

    enum class DurationClass(val value: String) {
        DURATION_0_5("0-5"),
        DURATION_5_10("5-10"),
        DURATION_10_15("10-15"),
        DURATION_15_00("15"),
        DURATION_LIVE("live"),
    }

    enum class PlaybackGoal constructor(val type: String, val hasValue: Boolean = false, var value: Any? = null) {
        NEW_MATERIAL("Odtwarzanie/Nowy_Materiał"),
        CONTACT("Odtwarzanie/Kontakt_Gemius_Stream"),
        START("Odtwarzanie/Start_Materiału_Głównego"),
        STOP("Odtwarzanie/Stop"),
        PERCENT_90_PERCENT("Odtwarzanie/90_Procent"),
        END("Odtwarzanie/Koniec_Materiału"),
        CYCLE("Odtwarzanie/Podtrzymanie"),
        SEEK("Odtwarzanie/Przewijanie"),
        SEEK_LIVE("Odtwarzanie/Przewijanie/", true),
        PAUSE("Odtwarzanie/Pauza"),
        UNPAUSE("Odtwarzanie/Start"),
        CHANGE_QUALITY("Odtwarzanie/Zmiana_Jakości"),
        CHANGE_VOLUME("Odtwarzanie/Zmiana_Głośności"),
        BUFFERING("Odtwarzanie/Buforowanie"),

        PREROLL_BLOCK_BEGIN("Odtwarzanie/PreRoll_StartBloku"),
        PREROLL_BEGIN("Odtwarzanie/PreRoll"),
        PREROLL_BLOCK_END("Odtwarzanie/PreRoll_KoniecBloku"),

        MIDROLL_BLOCK_BEGIN("Odtwarzanie/MidRoll_StartBloku"),
        MIDROLL_BEGIN("Odtwarzanie/MidRoll"),
        MIDROLL_BLOCK_END("Odtwarzanie/MidRoll_KoniecBloku"),

        POSTROLL_BLOCK_BEGIN("Odtwarzanie/PostRoll_StartBloku"),
        POSTROLL_BEGIN("Odtwarzanie/PostRoll"),
        POSTROLL_BLOCK_END("Odtwarzanie/PostRoll_KoniecBloku")
    }

    fun toUrl(): String {
        val urlParamsBuilder = StringBuilder()
        try {
            urlParamsBuilder.append("id=$id")
            urlParamsBuilder.append("&href=${GemiusPrismUtils.urlEncode(hrefParamsUrl)}")
            urlParamsBuilder.append("&extra=${GemiusPrismUtils.urlEncode(extraParamsUrl)}")
            urlParamsBuilder.append("&et=${eventType.type}")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return urlParamsBuilder.toString()
    }
}