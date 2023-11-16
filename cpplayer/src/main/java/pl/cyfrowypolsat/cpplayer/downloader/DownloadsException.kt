package pl.cyfrowypolsat.cpplayer.downloader

private const val USER_UNLOGGED_EXCEPTION = 801
private const val NO_DOWNLOAD_DATA_ERROR = 802

class UserUnloggedException() : RuntimeException("Downloads are not available for unlogged user") {
    val errorCode: String =  USER_UNLOGGED_EXCEPTION.toString()
}
class NoDownloadDataException(mediaId: String) : RuntimeException("No download data for media: {id: $mediaId}") {
    val errorCode: String =  NO_DOWNLOAD_DATA_ERROR.toString()
}