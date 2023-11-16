package pl.cyfrowypolsat.cpdata.api.payments.response


data class DotpayPaymentCardRegistrationDataResult(val urlc: String,
                                                   val sellerId: Int,
                                                   val amount: Double,
                                                   val currency: String,
                                                   val description: String,
                                                   val control: String,
                                                   val lang: String,
                                                   val channelGroups: List<String>?,
                                                   val firstName: String?,
                                                   val lastName: String?,
                                                   val paymentCardRegistrationUrl: String?,
                                                   val email: String?)