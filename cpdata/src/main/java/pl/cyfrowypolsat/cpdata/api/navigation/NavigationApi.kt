package pl.cyfrowypolsat.cpdata.api.navigation

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.ChannelProgramItemResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CommonlyAccessiblePacketResult
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.*
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetChannelsCurrentProgramParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetChannelsProgramParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetTvChannelProgramItemParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.favorites.GetFavoritesWithFlatNavigationParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.live.GetLiveChannelsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.live.GetLiveChannelsWithTreeNavigationParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.media.GetMediaListParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.media.GetMediaParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.media.GetMediaRelatedContentParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.packet.GetPacketContentParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.packet.GetPacketContentWithFlatNavigationParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.packet.GetPacketTreeNavigationParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.preplaydata.PrePlayDataParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.*
import pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation.GetStaffRecommendationsListItemsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation.GetStaffRecommendationsListsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.search.SearchParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.tvchannel.GetTvChannelsParams
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.*
import pl.cyfrowypolsat.cpdata.api.navigation.response.favorites.GetFavoritesTreeNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.favorites.GetFavoritesWithFlatNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.homemenu.HomeMenuItemResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.live.GetLiveChannelsResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.live.GetLiveChannelsWithTreeNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaRelatedContentResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.packet.GetPacketContentResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.packet.GetPacketContentWithFlatNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.packet.GetPacketTreeNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PrePlayDataResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.product.BundleResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.product.OfferResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.product.ProductResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.recommendation.GetStaffRecommendationsListItemsResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.recommendation.GetStaffRecommendationsListResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.search.SearchContentResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel.GetTvChannelsFlatNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel.GetTvChannelsResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel.GetTvChannelsTreeNavigationResult
import pl.cyfrowypolsat.cpdata.api.system.response.NavigationServiceConfig
import retrofit2.adapter.rxjava3.Result
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url


interface NavigationApi {

    // Home Menu
    @POST
    @JsonRPC(NavigationServiceConfig.GET_HOME_MENU)
    fun getHomeMenu(@Url url: String, @Body params: JsonRPCParams): Observable<List<HomeMenuItemResult>>


    // PrePlayData
    @POST
    @JsonRPC(NavigationServiceConfig.PRE_PLAY_DATA)
    fun prePlayData(@Url url: String, @Body params: PrePlayDataParams): Observable<PrePlayDataResult>


    // Media
    @POST
    @JsonRPC(NavigationServiceConfig.GET_MEDIA)
    fun getMedia(@Url url: String, @Body getMediaParams: GetMediaParams): Observable<GetMediaResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_MEDIA_LIST)
    fun getMediaList(@Url url: String, @Body getMediaListParams: GetMediaListParams): Observable<List<GetMediaResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_MEDIA_RELATED_CONTENT)
    fun getMediaRelatedContent(@Url url: String, @Body params: GetMediaRelatedContentParams): Observable<GetMediaRelatedContentResult>


    // Product
    @POST
    @JsonRPC(NavigationServiceConfig.GET_PRODUCT)
    fun getProduct(@Url url: String, @Body productParams: ProductParams): Observable<ProductResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_PRODUCTS)
    fun getProducts(@Url url: String, @Body productsParams: ProductsParams): Observable<List<ProductResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_MULTIPLE_PRODUCTS)
    fun getMultipleProducts(@Url url: String, @Body params: GetMultipleProductsParams): Observable<List<ProductResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_PURCHASE_CODE_PRODUCT)
    fun getPurchaseCodeProduct(@Url url: String, @Body getPurchaseCodeProductParams: GetPurchaseCodeProductParams): Observable<ProductResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_BUNDLES)
    fun getBundles(@Url url: String, @Body getBundlesParams: GetBundlesParams): Observable<List<BundleResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_OFFER)
    fun getOffer(@Url url: String, @Body offerParams: OfferParams): Observable<OfferResult>


    // Category
    @POST
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY)
    fun getCategory(@Url url: String,
                    @Body params: GetCategoryParams): Observable<GetCategoryResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_SUB_CATEGORIES_WITH_BASIC_NAVIGATION)
    fun getSubCategoriesWithBasicNavigation(@Url url: String,
                                            @Body params: GetSubCategoriesWithBasicNavigationParams): Observable<List<CategoryWithBasicNavigationResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY_WITH_FLAT_NAVIGATION)
    fun getCategoryWithFlatNavigation(@Url url: String,
                                      @Body params: GetCategoryWithFlatNavigationParams): Observable<CategoryWithFlatNavigationResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY_CONTENT)
    fun getCategoryContent(@Url url: String,
                           @Body params: GetCategoryContentParams): Observable<GetCategoryContentResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_SUB_CATEGORIES)
    fun getSubCategories(@Url url: String,
                         @Body params: GetSubCategoriesParams): Observable<List<CategoryResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY_CONTENT_AUTOPLAY_MEDIA_ID)
    fun getCategoryContentAutoplayMediaId(@Url url: String,
                                          @Body params: GetCategoryContentAutoplayMediaIdParams): Observable<GetCategoryContentAutoplayMediaIdResult>


    // Recommendation
    @POST
    @JsonRPC(NavigationServiceConfig.GET_STAFF_RECOMMENDATIONS_LISTS)
    fun getStaffRecommendationsLists(@Url url: String,
                                     @Body params: GetStaffRecommendationsListsParams): Observable<List<GetStaffRecommendationsListResult>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_STAFF_RECOMMENDATIONS_LIST_ITEMS)
    fun getStaffRecommendationsListItems(@Url url: String,
                                         @Body params: GetStaffRecommendationsListItemsParams): Observable<GetStaffRecommendationsListItemsResult>


    // Search
    @POST
    @JsonRPC(NavigationServiceConfig.SEARCH_CONTENT)
    fun searchContent(@Url url: String,
                      @Body params: SearchParams): Observable<SearchContentResult>


    // Packet
    @POST
    @JsonRPC(NavigationServiceConfig.GET_PACKET_TREE_NAVIGATION)
    fun getPacketTreeNavigation(@Url url: String,
                                @Body params: GetPacketTreeNavigationParams): Observable<GetPacketTreeNavigationResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_PACKET_CONTENT)
    fun getPacketContent(@Url url: String,
                         @Body params: GetPacketContentParams): Observable<GetPacketContentResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_PACKET_CONTENT_WITH_FLAT_NAVIGATION)
    fun getPacketContentWithFlatNavigation(@Url url: String,
                                           @Body params: GetPacketContentWithFlatNavigationParams): Observable<GetPacketContentWithFlatNavigationResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_COMMONLY_ACCESSIBLE_PACKETS)
    fun getCommonlyAccessiblePackets(@Url url: String,
                                     @Body params: JsonRPCParams): Observable<List<CommonlyAccessiblePacketResult>>


    // Live
    @POST
    @JsonRPC(NavigationServiceConfig.GET_LIVE_CHANNELS)
    fun getLiveChannels(@Url url: String,
                        @Body params: GetLiveChannelsParams): Observable<GetLiveChannelsResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_LIVE_CHANNELS_WITH_TREE_NAVIGATION)
    fun getLiveChannelsWithTreeNavigation(@Url url: String,
                                          @Body params: GetLiveChannelsWithTreeNavigationParams): Observable<GetLiveChannelsWithTreeNavigationResult>


    // Tv Channel
    @POST
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNELS_FLAT_NAVIGATION)
    fun getTvChannelsFlatNavigation(@Url url: String,
                                    @Body params: JsonRPCParams): Observable<GetTvChannelsFlatNavigationResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNELS_TREE_NAVIGATION)
    fun getTvChannelsTreeNavigation(@Url url: String,
                                    @Body params: JsonRPCParams): Observable<GetTvChannelsTreeNavigationResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNELS)
    fun getTvChannels(@Url url: String,
                      @Body params: GetTvChannelsParams): Observable<Result<GetTvChannelsResult>>


    // Tv Channel Program
    @POST
    @JsonRPC(NavigationServiceConfig.GET_CHANNELS_CURRENT_PROGRAM)
    fun getChannelsCurrentProgram(@Url url: String,
                                  @Body params: GetChannelsCurrentProgramParams): Observable<Result<Map<String, List<ChannelProgramItemResult>>>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_CHANNELS_PROGRAM)
    fun getChannelsProgram(@Url url: String,
                           @Body params: GetChannelsProgramParams): Observable<Result<Map<String, List<ChannelProgramItemResult>>>>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNEL_PROGRAM_ITEM)
    fun getTvChannelProgramItem(@Url url: String,
                                @Body params: GetTvChannelProgramItemParams): Observable<ChannelProgramItemResult>

    // Favorites
    @POST
    @JsonRPC(NavigationServiceConfig.GET_FAVORITES_TREE_NAVIGATION)
    fun getFavoritesTreeNavigation(@Url url: String,
                                   @Body params: JsonRPCParams): Observable<GetFavoritesTreeNavigationResult>

    @POST
    @JsonRPC(NavigationServiceConfig.GET_FAVORITES_WITH_FLAT_NAVIGATION)
    fun getFavoritesWithFlatNavigation(@Url url: String,
                                       @Body params: GetFavoritesWithFlatNavigationParams): Observable<GetFavoritesWithFlatNavigationResult>

}
