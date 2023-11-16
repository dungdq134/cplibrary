package pl.cyfrowypolsat.cpdata.api.system.response

import com.google.gson.annotations.SerializedName
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam


data class ConfigurationResponse(val time: Long,
                                 val ipData: IpData,
                                 val userAgent: String,
                                 val navigation: Navigation,
                                 val services: Services,
                                 val payments: Payments?,
                                 val reporting: Reporting?,
                                 val chromecast: Chromecast?,
                                 val auth: Auth?,
                                 val ntpServer: String?,
                                 val imgGeneratorCDN: ImgGeneratorCDN?,
                                 val maintenanceMode: MaintenanceMode?)

data class Navigation(val rootCategoryId: Int,
                      val movieCategoryId: Int?)

data class Payments(val hboProduct: ProductIdParam?,
                    val elevenProduct: ProductIdParam?,
                    val noAdsProduct: ProductIdParam?,
                    val plusProduct: ProductIdParam?)

data class Services(@SerializedName(AUTH_NAMESPACE) val auth: AuthServiceConfig,
                    @SerializedName(DRM_NAMESPACE) val drm: DrmServiceConfig,
                    @SerializedName(NAVIGATION_NAMESPACE) val navigation: NavigationServiceConfig,
                    @SerializedName(PAYMENTS_NAMESPACE) val payments: PaymentsServiceConfig,
                    @SerializedName(SYSTEM_NAMESPACE) val system: SystemServiceConfig,
                    @SerializedName(USER_CONTENT_NAMESPACE) val userContent: UserContentConfig) {
    companion object {
        const val AUTH_NAMESPACE = "auth"
        const val DRM_NAMESPACE = "drm"
        const val NAVIGATION_NAMESPACE = "navigation"
        const val PAYMENTS_NAMESPACE = "payments"
        const val SYSTEM_NAMESPACE = "system"
        const val USER_CONTENT_NAMESPACE = "user_content"

        fun supportedNamespaceList(): List<String> {
            return listOf(AUTH_NAMESPACE,
                    DRM_NAMESPACE,
                    NAVIGATION_NAMESPACE,
                    PAYMENTS_NAMESPACE,
                    SYSTEM_NAMESPACE,
                    USER_CONTENT_NAMESPACE)
        }
    }
}

data class Version(@SerializedName("1.0") val firstVersion: Service,
                   @SerializedName("2.0") val secondVersion: Service)

data class Service(val url: String,
                   val getUrl: String?)


data class AuthServiceConfig(@SerializedName(ACCEPT_RULES) val acceptRules: Version,
                             @SerializedName(CHANGE_EMAIL) val changeEmail: Version,
                             @SerializedName(CHANGE_PASSWORD) val changePassword: Version,
                             @SerializedName(CHECK_PIN) val checkPin: Version,
                             @SerializedName(CHECK_PLUS) val checkPlus: Version,
                             @SerializedName(CONFIRM_EMAIL_CHANGE) val confirmEmailChange: Version,
                             @SerializedName(CONFIRM_PLUS_CONNECTION) val confirmPlusConnection: Version,
                             @SerializedName(CONFIRM_REGISTRATION) val confirmRegistration: Version,
                             @SerializedName(CONFIRM_VERIFICATION) val confirmVerification: Version,
                             @SerializedName(CONNECT_PLUS) val connectPlus: Version,
                             @SerializedName(CONNECT) val connect: Version,
                             @SerializedName(CONFIRM_CONNECTION) val confirmConnection: Version,
                             @SerializedName(DELETE_USER) val deleteUser: Version,
                             @SerializedName(DISCONNECT_PLUS) val disconnectPlus: Version,
                             @SerializedName(DISCONNECT) val disconnect: Version,
                             @SerializedName(GET_ALL_RULES) val getAllRules: Version,
                             @SerializedName(GET_DEVICE_LIST) val getDeviceList: Version,
                             @SerializedName(GET_RULES) val getRules: Version,
                             @SerializedName(GET_RULES_STATUS) val getRulesStatus: Version,
                             @SerializedName(GET_SESSION) val getSession: Version,
                             @SerializedName(GET_UNACCEPTED_RULES) val getUnacceptedRules: Version,
                             @SerializedName(REQUEST_LOGIN) val requestLogin: Version,
                             @SerializedName(GET_LOGIN_REQUEST_STATUS) val getLoginRequestStatus: Version,
                             @SerializedName(LOGIN) val login: Version,
                             @SerializedName(LOGOUT) val logout: Version,
                             @SerializedName(REGISTER) val register: Version,
                             @SerializedName(REQUEST_REGISTER_CODE) val requestRegisterCode: Version,
                             @SerializedName(ACCEPT_REGISTER_CODE) val acceptRegisterCode: Version,
                             @SerializedName(REJECT_REGISTRATION) val rejectRegistration: Version,
                             @SerializedName(REQUEST_PASSWORD_RESET) val requestPasswordReset: Version,
                             @SerializedName(RESET_PASSWORD) val resetPassword: Version,
                             @SerializedName(SET_DEVICE_NAME) val setDeviceName: Version,
                             @SerializedName(SET_PARENTAL_CONTROLS) val setParentalControls: Version,
                             @SerializedName(SET_PIN) val setPin: Version,
                             @SerializedName(UNACCEPT_RULES) val unacceptRules: Version,
                             @SerializedName(UNVERIFY) val unverify: Version,
                             @SerializedName(GET_PROFILES) val getProfiles: Version,
                             @SerializedName(CREATE_PROFILE) val createProfile: Version,
                             @SerializedName(UPDATE_PROFILE) val updateProfile: Version,
                             @SerializedName(SET_SESSION_PROFILE) val setSessionProfile: Version,
                             @SerializedName(DELETE_PROFILE) val deleteProfile: Version,
                             @SerializedName(VERIFY) val verify: Version) {
    companion object {
        const val ACCEPT_RULES = "acceptRules"
        const val CHANGE_EMAIL = "changeEmail"
        const val CHANGE_PASSWORD = "changePassword"
        const val CHECK_PIN = "checkPin"
        const val CHECK_PLUS = "checkPlus"
        const val CONFIRM_EMAIL_CHANGE = "confirmEmailChange"
        const val CONFIRM_PLUS_CONNECTION = "confirmPlusConnection"
        const val CONFIRM_REGISTRATION = "confirmRegistration"
        const val CONFIRM_VERIFICATION = "confirmVerification"
        const val CONNECT_PLUS = "connectPlus"
        const val CONNECT = "connect"
        const val CONFIRM_CONNECTION = "confirmConnection"
        const val DELETE_USER = "deleteUser"
        const val DISCONNECT_PLUS = "disconnectPlus"
        const val DISCONNECT = "disconnect"
        const val GET_ALL_RULES = "getAllRules"
        const val GET_DEVICE_LIST = "getDeviceList"
        const val GET_RULES = "getRules"
        const val GET_RULES_STATUS = "getRulesStatus"
        const val GET_SESSION = "getSession"
        const val GET_UNACCEPTED_RULES = "getUnacceptedRules"
        const val REQUEST_LOGIN = "requestLogin"
        const val GET_LOGIN_REQUEST_STATUS = "getLoginRequestStatus"
        const val LOGIN = "login"
        const val LOGOUT = "logout"
        const val REGISTER = "register"
        const val REQUEST_REGISTER_CODE = "requestRegisterCode"
        const val ACCEPT_REGISTER_CODE = "acceptRegisterCode"
        const val REJECT_REGISTRATION = "rejectRegistration"
        const val REQUEST_PASSWORD_RESET = "requestPasswordReset"
        const val RESET_PASSWORD = "resetPassword"
        const val SET_DEVICE_NAME = "setDeviceName"
        const val SET_PARENTAL_CONTROLS = "setParentalControls"
        const val SET_PIN = "setPin"
        const val UNACCEPT_RULES = "unacceptRules"
        const val UNVERIFY = "unverify"
        const val GET_PROFILES = "getProfiles"
        const val CREATE_PROFILE = "createProfile"
        const val UPDATE_PROFILE = "updateProfile"
        const val SET_SESSION_PROFILE = "setSessionProfile"
        const val DELETE_PROFILE = "deleteProfile"
        const val VERIFY = "verify"
    }
}


data class DrmServiceConfig(@SerializedName(CHECK_PRODUCT_ACCESS) val checkProductAccess: Version,
                            @SerializedName(CHECK_PRODUCTS_ACCESS) val checkProductsAccess: Version,
                            @SerializedName(GET_PSEUDO_LICENSE) val getPseudoLicense: Version,
                            @SerializedName(GET_WIDEVINE_LICENSE) val getWidevineLicense: Version,
                            @SerializedName(STOP_PLAYBACK) val stopPlayback: Version,
                            @SerializedName(GET_USER_PLAYBACKS) val getUserPlaybacks: Version,
                            @SerializedName(CHECK_PRODUCT_EXTERNAL_ACTIVATION) val checkProductExternalActivation: Version,
                            @SerializedName(GET_PRODUCT_EXTERNAL_ACTIVATION_DATA) val getProductExternalActivationData: Version) {
    companion object {
        const val CHECK_PRODUCT_ACCESS = "checkProductAccess"
        const val CHECK_PRODUCTS_ACCESS = "checkProductsAccess"
        const val GET_PSEUDO_LICENSE = "getPseudoLicense"
        const val GET_WIDEVINE_LICENSE = "getWidevineLicense"
        const val STOP_PLAYBACK = "stopPlayback"
        const val GET_USER_PLAYBACKS = "getUserPlaybacks"
        const val CHECK_PRODUCT_EXTERNAL_ACTIVATION = "checkProductExternalActivation"
        const val GET_PRODUCT_EXTERNAL_ACTIVATION_DATA = "getProductExternalActivationData"
    }
}


data class NavigationServiceConfig(@SerializedName(GET_ALL_BUNDLES) val getAllBundles: Version,
                                   @SerializedName(GET_BUNDLES) val getBundles: Version,
                                   @SerializedName(GET_CATEGORIES) val getCategories: Version,
                                   @SerializedName(GET_CATEGORY) val getCategory: Version,
                                   @SerializedName(GET_CATEGORY_CONTENT) val getCategoryContent: Version,
                                   @SerializedName(GET_CATEGORY_CONTENT_AUTOPLAY_MEDIA_ID) val getCategoryContentAutoplayMediaId: Version,
                                   @SerializedName(GET_CATEGORY_CONTENT_WITH_FLAT_NAVIGATION) val getCategoryContentWithFlatNavigation: Version,
                                   @SerializedName(GET_CATEGORY_CONTENT_WITH_TREE_NAVIGATION) val getCategoryContentWithTreeNavigation: Version,
                                   @SerializedName(GET_CATEGORY_LIVE_CONTENT) val getCategoryLiveContent: Version,
                                   @SerializedName(GET_CATEGORY_WITH_BASIC_NAVIGATION) val getCategoryWithBasicNavigation: Version,
                                   @SerializedName(GET_CATEGORY_WITH_FLAT_NAVIGATION) val getCategoryWithFlatNavigation: Version,
                                   @SerializedName(GET_CATEGORY_WITH_TREE_NAVIGATION) val getCategoryWithTreeNaviagation: Version,
                                   @SerializedName(GET_CHANNELS_CURRENT_PROGRAM) val getChannelsCurrentProgram: Version,
                                   @SerializedName(GET_CHANNELS_PROGRAM) val getChannelsProgram: Version,
                                   @SerializedName(GET_CUSTOM_CONTENT) val getCustomContent: Version,
                                   @SerializedName(GET_CUSTOM_CONTENT_LISTS) val getCustomContentLists: Version,
                                   @SerializedName(GET_CUSTOM_CONTENT_WITH_FLAT_NAVIGATION) val getCustomContentWithFlatNavigation: Version,
                                   @SerializedName(GET_CUSTOM_CONTENT_WITH_TREE_NAVIGATION) val getCustomContentWithTreeNavigation: Version,
                                   @SerializedName(GET_LIVE_CHANNELS) val getLiveChannels: Version,
                                   @SerializedName(GET_LIVE_CHANNELS_BASIC_NAVIGATION) val getLiveChannelsBasicNavigation: Version,
                                   @SerializedName(GET_LIVE_CHANNELS_FLAT_NAVIGATION) val getLiveChannelsFlatNavigation: Version,
                                   @SerializedName(GET_LIVE_CHANNELS_TREE_NAVIGATION) val getLiveChannelsTreeNavigation: Version,
                                   @SerializedName(GET_LIVE_CHANNELS_WITH_FLAT_NAVIGATION) val getLiveChannelsWithFlatNavigation: Version,
                                   @SerializedName(GET_LIVE_CHANNELS_WITH_TREE_NAVIGATION) val getLiveChannelsWithTreeNavigation: Version,
                                   @SerializedName(GET_MEDIA) val getMedia: Version,
                                   @SerializedName(GET_MEDIA_LIST) val getMediaList: Version,
                                   @SerializedName(GET_MEDIA_RELATED_CONTENT) val getMediaRelatedContent: Version,
                                   @SerializedName(GET_MULTIPLE_PRODUCTS) val getMultipleProducts: Version,
                                   @SerializedName(GET_PACKET_BASIC_NAVIGATION) val getPacketBasicNavigation: Version,
                                   @SerializedName(GET_PACKET_CONTENT) val getPacketContent: Version,
                                   @SerializedName(GET_PACKET_CONTENT_WITH_FLAT_NAVIGATION) val getPacketContentWithFlatNavigation: Version,
                                   @SerializedName(GET_PACKET_CONTENT_WITH_TREE_NAVIGATION) val getPacketContentWithTreeNavigation: Version,
                                   @SerializedName(GET_PACKET_FLAT_NAVIGATION) val getPacketFlatNavigation: Version,
                                   @SerializedName(GET_PACKET_TREE_NAVIGATION) val getPacketTreeNavigation: Version,
                                   @SerializedName(GET_PLATFORMS) val getPlatforms: Version,
                                   @SerializedName(GET_HOME_MENU) val getHomeMenu: Version,
                                   @SerializedName(GET_PRODUCT) val getProduct: Version,
                                   @SerializedName(GET_OFFER) val getOffer: Version,
                                   @SerializedName(GET_PRODUCT_BASIC_NAVIGATION) val getProductBasicNavigation: Version,
                                   @SerializedName(GET_PRODUCT_CONTENT) val getProductContent: Version,
                                   @SerializedName(GET_PRODUCT_CONTENT_WITH_FLAT_NAVIGATION) val getProductContentWithFlatNavigation: Version,
                                   @SerializedName(GET_PRODUCT_CONTENT_WITH_TREE_NAVIGATION) val getProductContentWithTreeNavigation: Version,
                                   @SerializedName(GET_PRODUCT_FLAT_NAVIGATION) val getProductFlatNavigation: Version,
                                   @SerializedName(GET_PRODUCT_TREE_NAVIGATION) val getProductTreeNavigation: Version,
                                   @SerializedName(GET_PRODUCTS) val getProducts: Version,
                                   @SerializedName(GET_PURCHASE_CODE_PRODUCT) val getPurchaseCodeProduct: Version,
                                   @SerializedName(GET_STAFF_RECOMMENDATIONS) val getStaffRecommendations: Version,
                                   @SerializedName(GET_STAFF_RECOMMENDATIONS_LIST_ITEMS) val getStaffRecommendationsListItems: Version,
                                   @SerializedName(GET_STAFF_RECOMMENDATIONS_LISTS) val getStaffRecommendationsLists: Version,
                                   @SerializedName(GET_SUB_CATEGORIES) val getSubCategories: Version,
                                   @SerializedName(GET_SUB_CATEGORIES_WITH_BASIC_NAVIGATION) val getSubCategoriesWithBasicNavigation: Version,
                                   @SerializedName(GET_SUB_CATEGORIES_WITH_FLAT_NAVIGATION) val getSubCategoriesWithFlatNavigation: Version,
                                   @SerializedName(GET_SUB_CATEGORIES_WITH_TREE_NAVIGATION) val getSubCategoriesWithTreeNavigation: Version,
                                   @SerializedName(GET_TV_CHANNEL_PROGRAM_ITEM) val getTvChannelProgramItem: Version,
                                   @SerializedName(GET_TV_CHANNELS) val getTvChannels: Version,
                                   @SerializedName(GET_TV_CHANNELS_BASIC_NAVIGATION) val getTvChannelsBasicNavigation: Version,
                                   @SerializedName(GET_TV_CHANNELS_FLAT_NAVIGATION) val getTvChannelsFlatNavigation: Version,
                                   @SerializedName(GET_TV_CHANNELS_TREE_NAVIGATION) val getTvChannelsTreeNavigation: Version,
                                   @SerializedName(GET_VAS_PRODUCTS) val getVasProducts: Version,
                                   @SerializedName(PRE_PLAY_DATA) val prePlayData: Version,
                                   @SerializedName(SEARCH_AUTOCOMPLETE) val searchAutocomplete: Version,
                                   @SerializedName(SEARCH_CONTENT) val searchContent: Version,
                                   @SerializedName(SEARCH_CONTENT_WITH_FLAT_NAVIGATION) val searchContentWithFlatNavigation: Version,
                                   @SerializedName(SEARCH_CONTENT_WITH_TREE_NAVIGATION) val searchContentWithTreeNavigation: Version,
                                   @SerializedName(GET_FAVORITES) val getFavorites: Version,
                                   @SerializedName(GET_FAVORITES_TREE_NAVIGATION) val getFavoritesTreeNavigation: Version,
                                   @SerializedName(GET_FAVORITES_WITH_FLAT_NAVIGATION) val getFavoritesWithFlatNavigation: Version,
                                   @SerializedName(GET_COMMONLY_ACCESSIBLE_PACKETS) val getCommonlyAccessiblePackets: Version) {
    companion object {
        const val GET_ALL_BUNDLES = "getAllBundles"
        const val GET_BUNDLES = "getBundles"
        const val GET_CATEGORIES = "getCategories"
        const val GET_CATEGORY = "getCategory"
        const val GET_CATEGORY_CONTENT = "getCategoryContent"
        const val GET_CATEGORY_CONTENT_AUTOPLAY_MEDIA_ID = "getCategoryContentAutoplayMediaId"
        const val GET_CATEGORY_CONTENT_WITH_FLAT_NAVIGATION = "getCategoryContentWithFlatNavigation"
        const val GET_CATEGORY_CONTENT_WITH_TREE_NAVIGATION = "getCategoryContentWithTreeNavigation"
        const val GET_CATEGORY_LIVE_CONTENT = "getCategoryLiveContent"
        const val GET_CATEGORY_WITH_BASIC_NAVIGATION = "getCategoryWithBasicNavigation"
        const val GET_CATEGORY_WITH_FLAT_NAVIGATION = "getCategoryWithFlatNavigation"
        const val GET_CATEGORY_WITH_TREE_NAVIGATION = "getCategoryWithTreeNavigation"
        const val GET_CHANNELS_CURRENT_PROGRAM = "getChannelsCurrentProgram"
        const val GET_CHANNELS_PROGRAM = "getChannelsProgram"
        const val GET_CUSTOM_CONTENT = "getCustomContent"
        const val GET_CUSTOM_CONTENT_LISTS = "getCustomContentLists"
        const val GET_CUSTOM_CONTENT_WITH_FLAT_NAVIGATION = "getCustomContentWithFlatNavigation"
        const val GET_CUSTOM_CONTENT_WITH_TREE_NAVIGATION = "getCustomContentWithTreeNavigation"
        const val GET_LIVE_CHANNELS = "getLiveChannels"
        const val GET_LIVE_CHANNELS_BASIC_NAVIGATION = "getLiveChannelsBasicNavigation"
        const val GET_LIVE_CHANNELS_FLAT_NAVIGATION = "getLiveChannelsFlatNavigation"
        const val GET_LIVE_CHANNELS_TREE_NAVIGATION = "getLiveChannelsTreeNavigation"
        const val GET_LIVE_CHANNELS_WITH_FLAT_NAVIGATION = "getLiveChannelsWithFlatNavigation"
        const val GET_LIVE_CHANNELS_WITH_TREE_NAVIGATION = "getLiveChannelsWithTreeNavigation"
        const val GET_MEDIA = "getMedia"
        const val GET_MEDIA_LIST = "getMediaList"
        const val GET_MEDIA_RELATED_CONTENT = "getMediaRelatedContent"
        const val GET_MULTIPLE_PRODUCTS = "getMultipleProducts"
        const val GET_PACKET_BASIC_NAVIGATION = "getPacketBasicNavigation"
        const val GET_PACKET_CONTENT = "getPacketContent"
        const val GET_PACKET_CONTENT_WITH_FLAT_NAVIGATION = "getPacketContentWithFlatNavigation"
        const val GET_PACKET_CONTENT_WITH_TREE_NAVIGATION = "getPacketContentWithTreeNavigation"
        const val GET_PACKET_FLAT_NAVIGATION = "getPacketFlatNavigation"
        const val GET_PACKET_TREE_NAVIGATION = "getPacketTreeNavigation"
        const val GET_PLATFORMS = "getPlatforms"
        const val GET_HOME_MENU = "getHomeMenu"
        const val GET_PRODUCT = "getProduct"
        const val GET_OFFER = "getOffer"
        const val GET_PRODUCT_BASIC_NAVIGATION = "getProductBasicNavigation"
        const val GET_PRODUCT_CONTENT = "getProductContent"
        const val GET_PRODUCT_CONTENT_WITH_FLAT_NAVIGATION = "getProductContentWithFlatNavigation"
        const val GET_PRODUCT_CONTENT_WITH_TREE_NAVIGATION = "getProductContentWithTreeNavigation"
        const val GET_PRODUCT_FLAT_NAVIGATION = "getProductFlatNavigation"
        const val GET_PRODUCT_TREE_NAVIGATION = "getProductTreeNavigation"
        const val GET_PRODUCTS = "getProducts"
        const val GET_PURCHASE_CODE_PRODUCT = "getPurchaseCodeProduct"
        const val GET_STAFF_RECOMMENDATIONS = "getStaffRecommendations"
        const val GET_STAFF_RECOMMENDATIONS_LIST_ITEMS = "getStaffRecommendationsListItems"
        const val GET_STAFF_RECOMMENDATIONS_LISTS = "getStaffRecommendationsLists"
        const val GET_SUB_CATEGORIES = "getSubCategories"
        const val GET_SUB_CATEGORIES_WITH_BASIC_NAVIGATION = "getSubCategoriesWithBasicNavigation"
        const val GET_SUB_CATEGORIES_WITH_FLAT_NAVIGATION = "getSubCategoriesWithFlatNavigation"
        const val GET_SUB_CATEGORIES_WITH_TREE_NAVIGATION = "getSubCategoriesWithTreeNavigation"
        const val GET_TV_CHANNEL_PROGRAM_ITEM = "getTvChannelProgramItem"
        const val GET_TV_CHANNELS = "getTvChannels"
        const val GET_TV_CHANNELS_BASIC_NAVIGATION = "getTvChannelsBasicNavigation"
        const val GET_TV_CHANNELS_FLAT_NAVIGATION = "getTvChannelsFlatNavigation"
        const val GET_TV_CHANNELS_TREE_NAVIGATION = "getTvChannelsTreeNavigation"
        const val GET_VAS_PRODUCTS = "getVasProducts"
        const val PRE_PLAY_DATA = "prePlayData"
        const val SEARCH_AUTOCOMPLETE = "searchAutocomplete"
        const val SEARCH_CONTENT = "searchContent"
        const val SEARCH_CONTENT_WITH_FLAT_NAVIGATION = "searchContentWithFlatNavigation"
        const val SEARCH_CONTENT_WITH_TREE_NAVIGATION = "searchContentWithTreeNavigation"
        const val GET_FAVORITES = "getFavorites"
        const val GET_FAVORITES_TREE_NAVIGATION = "getFavoritesTreeNavigation"
        const val GET_FAVORITES_WITH_FLAT_NAVIGATION = "getFavoritesWithFlatNavigation"
        const val GET_COMMONLY_ACCESSIBLE_PACKETS = "getCommonlyAccessiblePackets"
    }
}


data class PaymentsServiceConfig(@SerializedName(GET_PRE_PURCHASE_DATA) val getPrePurchaseData: Version,
                                 @SerializedName(BUY) val buy: Version,
                                 @SerializedName(CANCEL_PRODUCT_SUBSCRIPTION) val cancelProductSubscription: Version,
                                 @SerializedName(DELETE_PAYMENT_CARD) val deletePaymentCard: Version,
                                 @SerializedName(GET_BUNDLE_STATE) val getBundleState: Version,
                                 @SerializedName(GET_CP_WALLETS_OPTION_DATA) val getCpWalletsOptionData: Version,
                                 @SerializedName(GET_MULTIPLE_PRODUCTS) val getMultipleProducts: Version,
                                 @SerializedName(GET_OPTION_DATA) val getOptionData: Version,
                                 @SerializedName(GET_ORDER_ID) val getOrderId: Version,
                                 @SerializedName(GET_ORDER_STATUS) val getOrderStatus: Version,
                                 @SerializedName(GET_PAYMENT_CARD_REGISTRATION_ID) val getPaymentCardRegistrationId: Version,
                                 @SerializedName(GET_PAYMENT_CARD_REGISTRATION_STATUS) val getPaymentCardRegistrationStatus: Version,
                                 @SerializedName(GET_PAYMENT_CARDS) val getPaymentCards: Version,
                                 @SerializedName(SET_DEFAULT_PAYMENT_CARD) val setDefaultPaymentCard: Version,
                                 @SerializedName(GET_POSSESSIONS) val getPossessions: Version,
                                 @SerializedName(GET_PROCESSING_ORDERS) val getProcessingOrders: Version,
                                 @SerializedName(GET_PRODUCT_SUBSCRIPTIONS) val getProductSubscriptions: Version,
                                 @SerializedName(GET_VAS_PRODUCTS) val getVasProducts: Version,
                                 @SerializedName(IS_TRIAL_AVAILABLE) val isTrialAvailable: Version,
                                 @SerializedName(REGISTER_PAYMENT_CARD) val registerPaymentCard: Version,
                                 @SerializedName(RENEW_PRODUCT_SUBSCRIPTION) val renewProductSubscription: Version,
                                 @SerializedName(SET_BUNDLE_STATE) val setBundleState: Version,
                                 @SerializedName(SUBMIT_PURCHASE_CODE) val submitPurchaseCode: Version,
                                 @SerializedName(GET_PURCHASE_CODE_DATA) val getPurchaseCodeData: Version,
                                 @SerializedName(SUBMIT_RECEIPT) val submitReceipt: Version) {
    companion object {
        const val GET_PRE_PURCHASE_DATA = "getPrePurchaseData"
        const val BUY = "buy"
        const val CANCEL_PRODUCT_SUBSCRIPTION = "cancelProductSubscription"
        const val DELETE_PAYMENT_CARD = "deletePaymentCard"
        const val GET_BUNDLE_STATE = "getBundleState"
        const val GET_CP_WALLETS_OPTION_DATA = "getCpWalletsOptionData"
        const val GET_MULTIPLE_PRODUCTS = "getMultipleProducts"
        const val GET_OPTION_DATA = "getOptionData"
        const val GET_ORDER_ID = "getOrderId"
        const val GET_ORDER_STATUS = "getOrderStatus"
        const val GET_PAYMENT_CARD_REGISTRATION_ID = "getPaymentCardRegistrationId"
        const val GET_PAYMENT_CARD_REGISTRATION_STATUS = "getPaymentCardRegistrationStatus"
        const val GET_PAYMENT_CARDS = "getPaymentCards"
        const val SET_DEFAULT_PAYMENT_CARD = "setDefaultPaymentCard"
        const val GET_POSSESSIONS = "getPossessions"
        const val GET_PROCESSING_ORDERS = "getProcessingOrders"
        const val GET_PRODUCT_SUBSCRIPTIONS = "getProductSubscriptions"
        const val GET_VAS_PRODUCTS = "getVasProducts"
        const val IS_TRIAL_AVAILABLE = "isTrialAvailable"
        const val REGISTER_PAYMENT_CARD = "registerPaymentCard"
        const val RENEW_PRODUCT_SUBSCRIPTION = "renewProductSubscription"
        const val SET_BUNDLE_STATE = "setBundleState"
        const val SUBMIT_PURCHASE_CODE = "submitPurchaseCode"
        const val GET_PURCHASE_CODE_DATA = "getPurchaseCodeData"
        const val SUBMIT_RECEIPT = "submitReceipt"
    }
}


data class SystemServiceConfig(@SerializedName(GET_CLIENT_ID) val getClientId: Version,
                               @SerializedName(GET_CONFIGURATION) val getConfiguration: Version,
                               @SerializedName(GET_HELP) val getHelp: Version,
                               @SerializedName(GET_PLATFORMS) val getPlatforms: Version,
                               @SerializedName(GET_TIMESTAMP) val getTimestamp: Version,
                               @SerializedName(GET_UPDATE_STATUS) val getUpdateStatus: Version,
                               @SerializedName(GET_ACTIVITY_EVENTS_AUTH_TOKEN) val getActivityEventsAuthToken: Version,
                               @SerializedName(GET_PLAYER_EVENTS_AUTH_TOKEN) val getPlayerEventsAuthToken: Version,
                               @SerializedName(GET_CAC_AUTH_TOKEN) val getCacAuthToken: Version,
                               @SerializedName(GET_AVATARS) val getAvatars: Version,
                               @SerializedName(GET_CLIENT_CONTEXT_TOKEN) val getClientContextToken: Version) {
    companion object {
        const val GET_CLIENT_ID = "getClientId"
        const val GET_CONFIGURATION = "getConfiguration"
        const val GET_HELP = "getHelp"
        const val GET_PLATFORMS = "getPlatforms"
        const val GET_TIMESTAMP = "getTimestamp"
        const val GET_UPDATE_STATUS = "getUpdateStatus"
        const val GET_ACTIVITY_EVENTS_AUTH_TOKEN = "getActivityEventsAuthToken"
        const val GET_PLAYER_EVENTS_AUTH_TOKEN = "getPlayerEventsAuthToken"
        const val GET_CAC_AUTH_TOKEN = "getCacAuthToken"
        const val GET_AVATARS = "getAvatars"
        const val GET_CLIENT_CONTEXT_TOKEN = "getClientContextToken"
    }
}

data class IpData(val ip: String,
                  val continent: String?,
                  val country: String?,
                  val isEu: Boolean?,
                  val isVpn: Boolean?,
                  val isp: String?)

//Reporting
data class Reporting(
        val gprism: GemiusPrism?,
        val activityEvents: ActivityEvents?,
        val gaviews: GemiusAudienceViews?,
        val appsFlyer: AppsFlyer?)

data class GemiusPrism(val service: String,
                       val accounts: List<String>?)

data class ActivityEvents(val service: String,
                          val authToken: String,
                          val originator: String)

data class GemiusAudienceViews(val service: String,
                               val accounts: List<String>?)

data class AppsFlyer(val devKey: String)

//Auth
data class Auth(val userEmailRequired: Boolean?,
                val profiles: Profiles?)

data class Profiles(val isEnabled: Boolean?,
                    val maxCount: Int?,
                    val minDurationBetweenLastStartupAndProfileSelection: Long?)

data class UserContentConfig(@SerializedName(GET_WATCHED_CONTENT_DATA) val getWatchedContentData: Version,
                             @SerializedName(GET_WATCHED_CONTENT_DATA_LIST) val getWatchedContentDataList: Version,
                             @SerializedName(GET_LATELY_WATCHED_CONTENT_DATA_LIST) val getLatelyWatchedContentDataList: Version,
                             @SerializedName(ADD_TO_FAVORITES) val addToFavorites: Version,
                             @SerializedName(DELETE_FROM_FAVORITES) val deleteFromFavorites: Version,
                             @SerializedName(CHECK_FAVORITE) val checkFavorite: Version) {

    companion object {
        const val GET_WATCHED_CONTENT_DATA = "getWatchedContentData"
        const val GET_WATCHED_CONTENT_DATA_LIST = "getWatchedContentDataList"
        const val GET_LATELY_WATCHED_CONTENT_DATA_LIST = "getLatelyWatchedContentDataList"
        const val ADD_TO_FAVORITES = "addToFavorites"
        const val DELETE_FROM_FAVORITES = "deleteFromFavorites"
        const val CHECK_FAVORITE = "checkFavorite"
    }
}

data class Chromecast(val appId: String?,
                      val customNamespace: String?)

data class MaintenanceMode(val url: String?)

data class ImgGeneratorCDN(val cdnUrl: String?,
                           val key: String?)