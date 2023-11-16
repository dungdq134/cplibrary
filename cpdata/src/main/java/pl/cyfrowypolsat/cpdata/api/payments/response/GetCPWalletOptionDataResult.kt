package pl.cyfrowypolsat.cpdata.api.payments.response

data class GetCPWalletOptionDataResult(val cpWallets: List<CPWalletResult>)

data class CPWalletResult(val contractId: String,
                          val credit: Int,
                          val creditText: String)