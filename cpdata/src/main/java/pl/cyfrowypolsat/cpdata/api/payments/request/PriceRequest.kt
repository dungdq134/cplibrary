package pl.cyfrowypolsat.cpdata.api.payments.request

data class PriceRequest(val currency: String,
                        val value: Int)