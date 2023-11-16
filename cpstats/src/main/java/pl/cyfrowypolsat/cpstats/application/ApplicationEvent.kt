package pl.cyfrowypolsat.cpstats.application

import pl.cyfrowypolsat.cpstats.core.model.*
import java.util.*

enum class EventType {
    APP_START,
    APP_PAUSE,
    APP_RESUME,
    NAVIGATION,
    NAVIGATION_ERROR,
    LOGIN,
    LOGIN_ERROR,
    REGISTER,
    REGISTER_ERROR,
    LOGOUT,
    ITEM_CLICK,
    NON_FATAL_ERROR,
    RATE_APP,
}

open class ApplicationEvent(val eventType: EventType,
                            val applicationData: ApplicationData,
                            val eventDate: Date = Date(),
                            val eventId: String = UUID.randomUUID().toString())

class ApplicationLoginEvent(applicationData: ApplicationData,
                            val accountData: AccountData) : ApplicationEvent(EventType.LOGIN, applicationData)

class ApplicationLoginErrorEvent(applicationData: ApplicationData,
                                 val userType: UserType,
                                 val errorData: ErrorData) : ApplicationEvent(EventType.LOGIN_ERROR, applicationData)

class ApplicationRegisterEvent(applicationData: ApplicationData,
                               val accountData: AccountData) : ApplicationEvent(EventType.REGISTER, applicationData)

class ApplicationRegisterErrorEvent(applicationData: ApplicationData,
                                    val userType: UserType,
                                    val errorData: ErrorData) : ApplicationEvent(EventType.REGISTER_ERROR, applicationData)

class ApplicationNavigationEvent(applicationData: ApplicationData,
                                 val placeData: PlaceData) : ApplicationEvent(EventType.NAVIGATION, applicationData)

class ApplicationNavigationErrorEvent(applicationData: ApplicationData,
                                      val placeData: PlaceData,
                                      val errorData: ErrorData) : ApplicationEvent(EventType.NAVIGATION_ERROR, applicationData)

class ApplicationLogoutEvent(applicationData: ApplicationData) : ApplicationEvent(EventType.LOGOUT, applicationData)

class ApplicationStartEvent(applicationData: ApplicationData) : ApplicationEvent(EventType.APP_START, applicationData)

class ApplicationPauseEvent(applicationData: ApplicationData) : ApplicationEvent(EventType.APP_PAUSE, applicationData)

class ApplicationResumeEvent(applicationData: ApplicationData) : ApplicationEvent(EventType.APP_RESUME, applicationData)

class ApplicationItemClickEvent(applicationData: ApplicationData,
                                val placeData: PlaceData,
                                val itemData: ItemData,
                                val listData: ListData) : ApplicationEvent(EventType.ITEM_CLICK, applicationData)

class ApplicationNonFatalErrorEvent(applicationData: ApplicationData,
                                    val errorData: ErrorData) : ApplicationEvent(EventType.NON_FATAL_ERROR, applicationData)

class ApplicationRateAppActionEvent(applicationData: ApplicationData,
                                    val rateAppAction: RateAppAction) : ApplicationEvent(EventType.RATE_APP, applicationData)
