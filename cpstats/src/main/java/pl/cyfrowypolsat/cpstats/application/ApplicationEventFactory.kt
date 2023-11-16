package pl.cyfrowypolsat.cpstats.application

import pl.cyfrowypolsat.cpstats.core.model.*

class ApplicationEventFactory {
    companion object {
        fun applicationStartEvent(applicationData: ApplicationData): ApplicationEvent {
            return ApplicationStartEvent(applicationData)
        }

        fun applicationResumeEvent(applicationData: ApplicationData): ApplicationEvent {
            return ApplicationResumeEvent(applicationData)
        }

        fun applicationPauseEvent(applicationData: ApplicationData): ApplicationEvent {
            return ApplicationPauseEvent(applicationData)
        }

        fun loginSuccessEvent(applicationData: ApplicationData, accountData: AccountData): ApplicationEvent {
            return ApplicationLoginEvent(applicationData, accountData)
        }

        fun loginErrorEvent(applicationData: ApplicationData, userType: UserType, errorData: ErrorData): ApplicationEvent {
            return ApplicationLoginErrorEvent(applicationData, userType, errorData)
        }

        fun registerSuccessEvent(applicationData: ApplicationData, accountData: AccountData): ApplicationEvent {
            return ApplicationRegisterEvent(applicationData, accountData)
        }

        fun registerErrorEvent(applicationData: ApplicationData, userType: UserType, errorData: ErrorData): ApplicationEvent {
            return ApplicationRegisterErrorEvent(applicationData, userType, errorData)
        }

        fun navigationSuccessEvent(applicationData: ApplicationData, placeData: PlaceData): ApplicationEvent {
            return ApplicationNavigationEvent(applicationData, placeData)
        }

        fun navigationErrorEvent(applicationData: ApplicationData, placeData: PlaceData, errorData: ErrorData): ApplicationEvent {
            return ApplicationNavigationErrorEvent(applicationData, placeData, errorData)
        }

        fun logoutEvent(applicationData: ApplicationData): ApplicationEvent {
            return ApplicationLogoutEvent(applicationData)
        }

        fun itemClickEvent(applicationData: ApplicationData, placeData: PlaceData, itemData: ItemData, listData: ListData): ApplicationEvent {
            return ApplicationItemClickEvent(applicationData, placeData, itemData, listData)
        }

        fun nonFatalErrorEvent(applicationData: ApplicationData, errorData: ErrorData): ApplicationEvent {
            return ApplicationNonFatalErrorEvent(applicationData, errorData)
        }

        fun rateAppActionEvent(applicationData: ApplicationData, rateAppAction: RateAppAction): ApplicationEvent {
            return ApplicationRateAppActionEvent(applicationData, rateAppAction)
        }
    }
}