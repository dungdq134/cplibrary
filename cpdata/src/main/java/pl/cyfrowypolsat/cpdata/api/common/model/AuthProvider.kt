package pl.cyfrowypolsat.cpdata.api.common.model

import com.google.gson.annotations.SerializedName

enum class AuthProvider {
    @SerializedName(NATIVE_SERIALIZED_NAME)
    NATIVE,

    @SerializedName(PLUS_SERIALIZED_NAME)
    PLUS,

    @SerializedName(ICOK_SERIALIZED_NAME)
    ICOK,

    @SerializedName(FACEBOOK_SERIALIZED_NAME)
    FACEBOOK,

    @SerializedName(APPLE_SERIALIZED_NAME)
    APPLE,

    @SerializedName(NETIA_SERIALIZED_NAME)
    NETIA,

    @SerializedName(GOOGLE_SERIALIZED_NAME)
    GOOGLE,

    @SerializedName(SSO_SERIALIZED_NAME)
    SSO;

    companion object {
        const val NATIVE_SERIALIZED_NAME = "native"
        const val PLUS_SERIALIZED_NAME = "plus"
        const val ICOK_SERIALIZED_NAME = "icok"
        const val FACEBOOK_SERIALIZED_NAME = "facebook"
        const val NETIA_SERIALIZED_NAME = "netia"
        const val APPLE_SERIALIZED_NAME = "apple"
        const val GOOGLE_SERIALIZED_NAME = "google"
        const val SSO_SERIALIZED_NAME = "sso"
    }
}