package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class BuyParams(val product: ProductIdParam,
                     val offer: OfferRequest,
                     val option: BuyOptionRequest,
                     val orderId: String) : JsonRPCParams()

interface BuyOptionRequest {
    val id: String
    val type: String
}


data class CardBuyOptionRequest(override val id: String,
                                override val type: String,
                                val price: PriceRequest,
                                val paymentCard: PaymentCardRequest?) : BuyOptionRequest


data class BlikBuyOptionRequest(override val id: String,
                                override val type: String,
                                val price: PriceRequest,
                                val blikCode: String) : BuyOptionRequest


data class CPWalletBuyOptionRequest(override val id: String,
                                    override val type: String,
                                    val price: PriceRequest,
                                    val cpWallet: PaymentCPWalletRequest) : BuyOptionRequest

data class NetiaBuyOptionRequest(override val id: String,
                                 override val type: String,
                                 val price: PriceRequest) : BuyOptionRequest

data class BuyOptionWithPriceRequest(override val id: String,
                                     override val type: String,
                                     val price: PriceRequest) : BuyOptionRequest