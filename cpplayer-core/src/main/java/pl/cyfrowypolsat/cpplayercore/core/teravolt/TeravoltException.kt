package pl.cyfrowypolsat.cpplayercore.core.teravolt

import com.teravolt.mobile.tvx_video_plugin.models.TvxError

class TeravoltException(val code: Int, val error: TvxError, message: String) : Exception(message)