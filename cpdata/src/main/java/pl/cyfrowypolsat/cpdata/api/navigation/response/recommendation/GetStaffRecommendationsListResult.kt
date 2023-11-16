package pl.cyfrowypolsat.cpdata.api.navigation.response.recommendation

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult

data class GetStaffRecommendationsListResult(val id: String,
                                             val name: String?,
                                             val layout: Layout?)

data class Layout(val value: String?,
                  val customData: CustomData?)

data class CustomData(val imageDisplayMode: String?,
                      val imageDisplaySize: String?,
                      val listElementTitlePosition: String?,
                      val listElementTitleLines: Int?,
                      val h2TagsPosition: String?,
                      val descriptionPosition: String?,
                      val titleType: String?,
                      val titleAlign: String?,
                      val showListTitle: Boolean?,
                      val background: List<ImageSourceResult>?,
                      val ctaButtonLabel: String?,
                      val legal: String?,
                      val logo: ImageSourceResult?,
                      val listScroll: String?,
                      val rowCount: Int?,
                      val elementsAlign: String?)
