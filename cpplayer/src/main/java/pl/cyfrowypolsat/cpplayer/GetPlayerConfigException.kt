package pl.cyfrowypolsat.cpplayer

private const val PLAYER_CONFIG_NO_SUPPORTED_SOURCES_ERROR = 901
private const val PLAYER_CONFIG_NO_PSEUDO_LICENSE_SERVICE_ERROR = 902

class NoSupportedSourcesException(mediaId: String, mediaCpid: Int) : RuntimeException("No supported sources for media: {id: $mediaId, cpid: $mediaCpid}") {
    val errorCode: String = PLAYER_CONFIG_NO_SUPPORTED_SOURCES_ERROR.toString()
}

class NoPseudoLicenseServiceException(mediaId: String, mediaCpid: Int) : RuntimeException("No pseudoLicenseService for media: {id: $mediaId, cpid: $mediaCpid}") {
    val errorCode: String = PLAYER_CONFIG_NO_PSEUDO_LICENSE_SERVICE_ERROR.toString()
}
