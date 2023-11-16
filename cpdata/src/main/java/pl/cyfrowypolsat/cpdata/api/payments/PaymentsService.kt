package pl.cyfrowypolsat.cpdata.api.payments

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.GetPrePurchaseDataParams
import pl.cyfrowypolsat.cpdata.api.payments.request.*
import pl.cyfrowypolsat.cpdata.api.payments.response.*
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import javax.inject.Inject


class PaymentsService(private val configurationRepository: ConfigurationRepository) {

    @CpDataQualifier
    @Inject lateinit var paymentsApi: PaymentsApi

    init {
        CpData.getInstance().component.inject(this)
    }

    // PrePurchaseData
    fun getPrePurchaseData(getPrePurchaseDataParams: GetPrePurchaseDataParams): Observable<List<GetPrePurchaseDataResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getPrePurchaseData.firstVersion.url
                    paymentsApi.getPrePurchaseData(url, getPrePurchaseDataParams)
                }
    }


    // Possessions
    fun getActivePossessions(): Observable<List<PossessionResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getPossessions.firstVersion.url
                    paymentsApi.getPossessions(url, JsonRPCParams())
                            .map { possessionResults -> possessionResults.filter { it.status == "active" } }
                }
    }

    fun getPossessions(): Observable<List<PossessionResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getPossessions.firstVersion.url
                    paymentsApi.getPossessions(url, JsonRPCParams())
                }
    }


    // Subscriptions
    fun getProductSubscriptions(): Observable<List<ProductSubscriptionResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getProductSubscriptions.firstVersion.url
                    paymentsApi.getProductSubscriptions(url, JsonRPCParams())
                }
    }

    fun cancelProductSubscription(productSubscriptionId: String): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.cancelProductSubscription.firstVersion.url
                    paymentsApi.cancelProductSubscription(url, ProductSubscriptionParams(productSubscriptionId))
                }
    }

    fun renewProductSubscription(productSubscriptionId: String): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.renewProductSubscription.firstVersion.url
                    paymentsApi.renewProductSubscription(url, ProductSubscriptionParams(productSubscriptionId))
                }
    }


    // Order
    fun getOrderId(getOrderIdParams: GetOrderIdParams): Observable<String> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getOrderId.firstVersion.url
                    paymentsApi.getOrderId(url, getOrderIdParams)
                            .map { it.orderId }
                }
    }

    fun getOrderStatus(orderId: String): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getOrderStatus.firstVersion.url
                    paymentsApi.getOrderStatus(url, GetOrderStatusParams(orderId))
                }
    }


    // Purchase code
    fun submitPurchaseCode(submitPurchaseCodeParams: SubmitPurchaseCodeParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.submitPurchaseCode.firstVersion.url
                    paymentsApi.submitPurchaseCode(url, submitPurchaseCodeParams)
                }
    }

    fun getPurchaseCodeData(getPurchaseCodeDataParams: GetPurchaseCodeDataParams): Observable<GetPurchaseCodeDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getPurchaseCodeData.firstVersion.url
                    paymentsApi.getPurchaseCodeData(url, getPurchaseCodeDataParams)
                }
    }


    // Bundle
    fun getBundleState(bundleId: String): Observable<GetBundleStateResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getBundleState.firstVersion.url
                    paymentsApi.getBundleState(url, GetBundleStateParams(bundleId))
                }
    }

    fun setBundleState(params: SetBundleStateParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.setBundleState.firstVersion.url
                    paymentsApi.setBundleState(url, params)
                }
    }


    // Option data
    fun getSmsOptionData(params: GetOptionDataParams): Observable<GetSmsOptionDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getOptionData.firstVersion.url
                    paymentsApi.getSmsOptionData(url, params)
                }
    }

    fun getDotpayOptionData(params: GetOptionDataParams): Observable<GetDotpayOptionDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getOptionData.firstVersion.url
                    paymentsApi.getDotpayOptionData(url, params)
                }
    }

    fun getCPWalletOptionData(params: GetOptionDataParams): Observable<GetCPWalletOptionDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getOptionData.firstVersion.url
                    paymentsApi.getCPWalletOptionData(url, params)
                }
    }

    fun getNetiaOptionData(params: GetOptionDataParams): Observable<GetNetiaOptionDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getOptionData.firstVersion.url
                    paymentsApi.getNetiaOptionData(url, params)
                }
    }


    // Buy
    fun buy(params: BuyParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.buy.firstVersion.url
                    paymentsApi.buy(url, params)
                }
    }


    // Submit receipt
    fun submitReceipt(params: SubmitReceiptParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.submitReceipt.firstVersion.url
                    paymentsApi.submitReceipt(url, params)
                }
    }


    // Trial
    fun isTrialAvailable(isTrialAvailableParams: IsTrialAvailableParams): Observable<Boolean> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.isTrialAvailable.firstVersion.url
                    paymentsApi.isTrialAvailable(url, isTrialAvailableParams)
                            .map { result -> result.status == 0 }
                }
    }


    // Payment cards
    fun getPaymentCards(): Observable<List<GetPaymentCardsResult>> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val url = configurationResponse.services.payments.getPaymentCards.firstVersion.url
                paymentsApi.getPaymentCards(url, JsonRPCParams())
            }
    }

    fun setDefaultPaymentCard(paymentCardParams: PaymentCardParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.setDefaultPaymentCard.firstVersion.url
                    paymentsApi.setDefaultPaymentCard(url, paymentCardParams)
                }
    }

    fun getPaymentCardRegistrationId(jsonRPCParams: JsonRPCParams): Observable<GetPaymentCardRegistrationIdResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.getPaymentCardRegistrationId.firstVersion.url
                    paymentsApi.getPaymentCardRegistrationId(url, jsonRPCParams)
                }
    }

    fun registerPaymentCard(registerPaymentCardParams: RegisterPaymentCardParams): Observable<DotpayPaymentCardRegistrationDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.payments.registerPaymentCard.firstVersion.url
                    paymentsApi.registerPaymentCard(url, registerPaymentCardParams)
                }
    }



}
