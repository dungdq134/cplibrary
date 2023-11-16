package pl.cyfrowypolsat.cpdata.api.auth.request.common

data class Captcha(val type: String,
                   val value: String)