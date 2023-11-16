package pl.cyfrowypolsat.cpdata.api.common.session

interface AutoLoginFailedListener {

    fun onAutoLoginFailed(throwable: Throwable)
}