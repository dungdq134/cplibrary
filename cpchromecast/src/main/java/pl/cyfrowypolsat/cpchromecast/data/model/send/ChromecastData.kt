package pl.cyfrowypolsat.cpchromecast.data.model.send

import com.google.gson.annotations.SerializedName

data class StartPlayer(@SerializedName("type") val type: String = "startPlayer",
                       @SerializedName("forcePlay") var forcePlay: Boolean = false,
                       @SerializedName("data") var data: StartPlayerData? = null)

data class StartPlayerData(@SerializedName("user") var user: User? = null,
                           @SerializedName("media") var media: Media? = null,
                           @SerializedName("device") var device: String? = null,
                           @SerializedName("clientID") var clientID: String? = null,
                           @SerializedName("position") var startPosition: Int? = null,
                           @SerializedName("portal") var portal: String? = null)

data class User(@SerializedName("id") var id: String? = null,
                @SerializedName("session") var session: Session? = null,
                @SerializedName("profileId") var profileId: String? = null)

data class Session(@SerializedName("id") var id: String? = null,
                   @SerializedName("key") var key: String? = null,
                   @SerializedName("keyExpirationTime") var keyExpirationTime: Int = -1)

data class Media(@SerializedName("gmID") var gmID: GmID? = null)

data class GmID(@SerializedName("cpid") var cpid: Int = -1,
                @SerializedName("id") var id: String? = null)