package pl.cyfrowypolsat.cpdata.api.common.model.cpidobject

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam
import java.util.*

data class CommonlyAccessiblePacketResult(override val cpid: Int,
                                          val id: String,
                                          val name: String,
                                          val product: ProductIdParam,
                                          val description: String?,
                                          val thumbnails: List<ImageSourceResult>?,
                                          val validFrom: Date?,
                                          val validTo: Date?,
                                          val reporting: PacketReporting?) : CpidObject