package pl.cyfrowypolsat.cpstats.application.gemiusprism

import pl.cyfrowypolsat.cpstats.core.gemiusprism.GemiusPrismUtils

data class GemiusPrismHit constructor(val id: String,
                                      val hrefParamsUrl: String,
                                      val extraParamsUrl: String,
                                      val eventType: Type) {

    enum class Type constructor(val type: String) {
        ACTION("action"),
        VIEW("view")
    }

    enum class InterfaceEvent constructor(val type: String,
                                          val hasValue: Boolean = false,
                                          var value: Any? = null) {
        APP_BACKGROUND("Interfejs/Minimalizacja_Okna"),
        APP_FOREGROUND("Interfejs/Maksymalizacja_Okna"),
        FIRST_APP_START("Interfejs/Start_Aplikacji/Pierwszy"),
        APP_START("Interfejs/Start_Aplikacji/Kolejny"),
        LOGIN("Interfejs/Login"),
        LOGIN_EMAIL("Interfejs/Login/Email"),
        LOGIN_FACEBOOK("Interfejs/Login/FB"),
        LOGIN_ICOK("Interfejs/Login/ICOK"),
        LOGIN_PLUS("Interfejs/Login/PLUS"),
        LOGIN_APPLE("Interfejs/Login/APPLE"),
        LOGIN_GOOGLE("Interfejs/Login/GOOGLE"),
        LOGIN_SSO("Interfejs/Login/SSO"),
        LOGOUT("Interfejs/Wylogowanie"),
        NAVIGATION("Interfejs/Przeglądanie"),
        SEARCH("Interfejs/Wyszukiwanie"),
        PAYMENT_START("Interfejs/Kupuję"),
        PAYMENT_FINISHED("Interfejs/Kupilem/", true)
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