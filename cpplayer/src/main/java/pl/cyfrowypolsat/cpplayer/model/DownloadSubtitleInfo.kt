package pl.cyfrowypolsat.cpplayer.model

data class DownloadSubtitleInfo(val url: String,
                                val localFilePath: String,
                                val mimeType: String,
                                val language: String)