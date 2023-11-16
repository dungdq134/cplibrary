package pl.cyfrowypolsat.cpdata.api.payments

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.GetPrePurchaseDataParams
import pl.cyfrowypolsat.cpdata.api.payments.request.*
import pl.cyfrowypolsat.cpdata.api.payments.response.*
import pl.cyfrowypolsat.cpdata.api.system.response.PaymentsServiceConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentsApi {

    // PrePurchaseData
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_PRE_PURCHASE_DATA)
    fun getPrePurchaseData(@Url url: String, @Body params: GetPrePurchaseDataParams): Observable<List<GetPrePurchaseDataResult>>


    // Order
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_ORDER_ID)
    fun getOrderId(@Url url: String, @Body getOrderIdParams: GetOrderIdParams): Observable<GetOrderIdResult>

    @POST
    @JsonRPC(PaymentsServiceConfig.GET_ORDER_STATUS)
    fun getOrderStatus(@Url url: String, @Body getOrderStatusParams: GetOrderStatusParams): Observable<Result>


    // Purchase code
    @POST
    @JsonRPC(PaymentsServiceConfig.SUBMIT_PURCHASE_CODE)
    fun submitPurchaseCode(@Url url: String, @Body submitPurchaseCodeParams: SubmitPurchaseCodeParams): Observable<Result>

    @POST
    @JsonRPC(PaymentsServiceConfig.GET_PURCHASE_CODE_DATA)
    fun getPurchaseCodeData(@Url url: String, @Body getPurchaseCodeDataParams: GetPurchaseCodeDataParams): Observable<GetPurchaseCodeDataResult>


    // Possessions
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_POSSESSIONS)
    fun getPossessions(@Url url: String, @Body getPossessionsParams: JsonRPCParams): Observable<List<PossessionResult>>


    // Bundle
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_BUNDLE_STATE)
    fun getBundleState(@Url url: String, @Body getBundleStateParams: GetBundleStateParams): Observable<GetBundleStateResult>

    @POST
    @JsonRPC(PaymentsServiceConfig.SET_BUNDLE_STATE)
    fun setBundleState(@Url url: String, @Body setBundleStateParams: SetBundleStateParams): Observable<Result>


    // Subscriptions
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_PRODUCT_SUBSCRIPTIONS)
    fun getProductSubscriptions(@Url url: String, @Body params: JsonRPCParams): Observable<List<ProductSubscriptionResult>>

    @POST
    @JsonRPC(PaymentsServiceConfig.CANCEL_PRODUCT_SUBSCRIPTION)
    fun cancelProductSubscription(@Url url: String, @Body params: ProductSubscriptionParams): Observable<Result>

    @POST
    @JsonRPC(PaymentsServiceConfig.RENEW_PRODUCT_SUBSCRIPTION)
    fun renewProductSubscription(@Url url: String, @Body params: ProductSubscriptionParams): Observable<Result>


    // Option data
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_OPTION_DATA)
    fun getSmsOptionData(@Url url: String, @Body params: GetOptionDataParams): Observable<GetSmsOptionDataResult>

    @POST
    @JsonRPC(PaymentsServiceConfig.GET_OPTION_DATA)
    fun getDotpayOptionData(@Url url: String, @Body params: GetOptionDataParams): Observable<GetDotpayOptionDataResult>

    @POST
    @JsonRPC(PaymentsServiceConfig.GET_OPTION_DATA)
    fun getCPWalletOptionData(@Url url: String, @Body params: GetOptionDataParams): Observable<GetCPWalletOptionDataResult>

    @POST
    @JsonRPC(PaymentsServiceConfig.GET_OPTION_DATA)
    fun getNetiaOptionData(@Url url: String, @Body params: GetOptionDataParams): Observable<GetNetiaOptionDataResult>


    // Buy
    @POST
    @JsonRPC(PaymentsServiceConfig.BUY)
    fun buy(@Url url: String, @Body params: BuyParams): Observable<Result>


    // Submit receipt
    @POST
    @JsonRPC(PaymentsServiceConfig.SUBMIT_RECEIPT)
    fun submitReceipt(@Url url: String, @Body params: SubmitReceiptParams): Observable<Result>


    // Trial
    @POST
    @JsonRPC(PaymentsServiceConfig.IS_TRIAL_AVAILABLE)
    fun isTrialAvailable(@Url url: String, @Body params: IsTrialAvailableParams): Observable<Result>


    // Payment cards
    @POST
    @JsonRPC(PaymentsServiceConfig.GET_PAYMENT_CARDS)
    fun getPaymentCards(@Url url: String, @Body params: JsonRPCParams): Observable<List<GetPaymentCardsResult>>

    @POST
    @JsonRPC(PaymentsServiceConfig.SET_DEFAULT_PAYMENT_CARD)
    fun setDefaultPaymentCard(@Url url: String, @Body params: PaymentCardParams): Observable<Result>

    @POST
    @JsonRPC(PaymentsServiceConfig.GET_PAYMENT_CARD_REGISTRATION_ID)
    fun getPaymentCardRegistrationId(@Url url: String, @Body params: JsonRPCParams): Observable<GetPaymentCardRegistrationIdResult>

    @POST
    @JsonRPC(PaymentsServiceConfig.REGISTER_PAYMENT_CARD)
    fun registerPaymentCard(@Url url: String, @Body params: RegisterPaymentCardParams): Observable<DotpayPaymentCardRegistrationDataResult>

}
