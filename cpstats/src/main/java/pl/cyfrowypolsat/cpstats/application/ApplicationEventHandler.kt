package pl.cyfrowypolsat.cpstats.application

interface ApplicationEventHandler {

    fun handleEvent(event: ApplicationEvent)
}