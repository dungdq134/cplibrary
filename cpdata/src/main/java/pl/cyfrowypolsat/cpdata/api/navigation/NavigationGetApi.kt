package pl.cyfrowypolsat.cpdata.api.navigation

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.ChannelProgramItemResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CommonlyAccessiblePacketResult
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetCategoryContentAutoplayMediaIdParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetCategoryContentParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetCategoryParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetCategoryWithFlatNavigationParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetSubCategoriesParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetSubCategoriesWithBasicNavigationParams
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
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.GetBundlesParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.GetMultipleProductsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.GetPurchaseCodeProductParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.OfferParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.ProductParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.product.ProductsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation.GetStaffRecommendationsListItemsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation.GetStaffRecommendationsListsParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.search.SearchParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.tvchannel.GetTvChannelsParams
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.CategoryWithBasicNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.CategoryWithFlatNavigationResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.GetCategoryContentAutoplayMediaIdResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.GetCategoryContentResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.GetCategoryResult
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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url


interface NavigationGetApi {

    // Home Menu
    @GET
    @JsonRPC(NavigationServiceConfig.GET_HOME_MENU)
    fun getHomeMenu(@Url url: String,
                    @Query("method") method: String = NavigationServiceConfig.GET_HOME_MENU,
                    @Query("id") id : Int = 1,
                    @Query("params") params: JsonRPCParams): Observable<List<HomeMenuItemResult>>


    // PrePlayData
    @GET
    @JsonRPC(NavigationServiceConfig.PRE_PLAY_DATA)
    fun prePlayData(@Url url: String,
                    @Query("method") method: String = NavigationServiceConfig.PRE_PLAY_DATA,
                    @Query("id") id : Int = 1,
                    @Query("params") params: JsonRPCParams): Observable<PrePlayDataResult>


    // Media
    @GET
    @JsonRPC(NavigationServiceConfig.GET_MEDIA)
    fun getMedia(@Url url: String,
                 @Query("method") method: String = NavigationServiceConfig.GET_MEDIA,
                 @Query("id") id : Int = 1,
                 @Query("params") params: JsonRPCParams): Observable<GetMediaResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_MEDIA_LIST)
    fun getMediaList(@Url url: String,
                     @Query("method") method: String = NavigationServiceConfig.GET_MEDIA_LIST,
                     @Query("id") id : Int = 1,
                     @Query("params") params: JsonRPCParams): Observable<List<GetMediaResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_MEDIA_RELATED_CONTENT)
    fun getMediaRelatedContent(@Url url: String,
                               @Query("method") method: String = NavigationServiceConfig.GET_MEDIA_RELATED_CONTENT,
                               @Query("id") id : Int = 1,
                               @Query("params") params: JsonRPCParams): Observable<GetMediaRelatedContentResult>


    // Product
    @GET
    @JsonRPC(NavigationServiceConfig.GET_PRODUCT)
    fun getProduct(@Url url: String,
                   @Query("method") method: String = NavigationServiceConfig.GET_PRODUCT,
                   @Query("id") id : Int = 1,
                   @Query("params") params: JsonRPCParams): Observable<ProductResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_PRODUCTS)
    fun getProducts(@Url url: String,
                    @Query("method") method: String = NavigationServiceConfig.GET_PRODUCTS,
                    @Query("id") id : Int = 1,
                    @Query("params") params: JsonRPCParams): Observable<List<ProductResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_MULTIPLE_PRODUCTS)
    fun getMultipleProducts(@Url url: String,
                            @Query("method") method: String = NavigationServiceConfig.GET_MULTIPLE_PRODUCTS,
                            @Query("id") id : Int = 1,
                            @Query("params") params: JsonRPCParams): Observable<List<ProductResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_PURCHASE_CODE_PRODUCT)
    fun getPurchaseCodeProduct(@Url url: String,
                               @Query("method") method: String = NavigationServiceConfig.GET_PURCHASE_CODE_PRODUCT,
                               @Query("id") id : Int = 1,
                               @Query("params") params: JsonRPCParams): Observable<ProductResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_BUNDLES)
    fun getBundles(@Url url: String,
                   @Query("method") method: String = NavigationServiceConfig.GET_BUNDLES,
                   @Query("id") id : Int = 1,
                   @Query("params") params: JsonRPCParams): Observable<List<BundleResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_OFFER)
    fun getOffer(@Url url: String,
                 @Query("method") method: String = NavigationServiceConfig.GET_OFFER,
                 @Query("id") id : Int = 1,
                 @Query("params") params: JsonRPCParams): Observable<OfferResult>


    // Category
    @GET
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY)
    fun getCategory(@Url url: String,
                    @Query("method") method: String = NavigationServiceConfig.GET_CATEGORY,
                    @Query("id") id : Int = 1,
                    @Query("params") params: JsonRPCParams): Observable<GetCategoryResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_SUB_CATEGORIES_WITH_BASIC_NAVIGATION)
    fun getSubCategoriesWithBasicNavigation(@Url url: String,
                                            @Query("method") method: String = NavigationServiceConfig.GET_SUB_CATEGORIES_WITH_BASIC_NAVIGATION,
                                            @Query("id") id : Int = 1,
                                            @Query("params") params: JsonRPCParams): Observable<List<CategoryWithBasicNavigationResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY_WITH_FLAT_NAVIGATION)
    fun getCategoryWithFlatNavigation(@Url url: String,
                                      @Query("method") method: String = NavigationServiceConfig.GET_CATEGORY_WITH_FLAT_NAVIGATION,
                                      @Query("id") id : Int = 1,
                                      @Query("params") params: JsonRPCParams): Observable<CategoryWithFlatNavigationResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY_CONTENT)
    fun getCategoryContent(@Url url: String,
                           @Query("method") method: String = NavigationServiceConfig.GET_CATEGORY_CONTENT,
                           @Query("id") id : Int = 1,
                           @Query("params") params: JsonRPCParams): Observable<GetCategoryContentResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_SUB_CATEGORIES)
    fun getSubCategories(@Url url: String,
                         @Query("method") method: String = NavigationServiceConfig.GET_SUB_CATEGORIES,
                         @Query("id") id : Int = 1,
                         @Query("params") params: JsonRPCParams): Observable<List<CategoryResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_CATEGORY_CONTENT_AUTOPLAY_MEDIA_ID)
    fun getCategoryContentAutoplayMediaId(@Url url: String,
                                          @Query("method") method: String = NavigationServiceConfig.GET_CATEGORY_CONTENT_AUTOPLAY_MEDIA_ID,
                                          @Query("id") id : Int = 1,
                                          @Query("params") params: JsonRPCParams): Observable<GetCategoryContentAutoplayMediaIdResult>


    // Recommendation
    @GET
    @JsonRPC(NavigationServiceConfig.GET_STAFF_RECOMMENDATIONS_LISTS)
    fun getStaffRecommendationsLists(@Url url: String,
                                     @Query("method") method: String = NavigationServiceConfig.GET_STAFF_RECOMMENDATIONS_LISTS,
                                     @Query("id") id : Int = 1,
                                     @Query("params") params: JsonRPCParams): Observable<List<GetStaffRecommendationsListResult>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_STAFF_RECOMMENDATIONS_LIST_ITEMS)
    fun getStaffRecommendationsListItems(@Url url: String,
                                         @Query("method") method: String = NavigationServiceConfig.GET_STAFF_RECOMMENDATIONS_LIST_ITEMS,
                                         @Query("id") id : Int = 1,
                                         @Query("params") params: JsonRPCParams): Observable<GetStaffRecommendationsListItemsResult>


    // Search
    @GET
    @JsonRPC(NavigationServiceConfig.SEARCH_CONTENT)
    fun searchContent(@Url url: String,
                      @Query("method") method: String = NavigationServiceConfig.SEARCH_CONTENT,
                      @Query("id") id : Int = 1,
                      @Query("params") params: JsonRPCParams): Observable<SearchContentResult>


    // Packet
    @GET
    @JsonRPC(NavigationServiceConfig.GET_PACKET_TREE_NAVIGATION)
    fun getPacketTreeNavigation(@Url url: String,
                                @Query("method") method: String = NavigationServiceConfig.GET_PACKET_TREE_NAVIGATION,
                                @Query("id") id : Int = 1,
                                @Query("params") params: JsonRPCParams): Observable<GetPacketTreeNavigationResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_PACKET_CONTENT)
    fun getPacketContent(@Url url: String,
                         @Query("method") method: String = NavigationServiceConfig.GET_PACKET_CONTENT,
                         @Query("id") id : Int = 1,
                         @Query("params") params: JsonRPCParams): Observable<GetPacketContentResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_PACKET_CONTENT_WITH_FLAT_NAVIGATION)
    fun getPacketContentWithFlatNavigation(@Url url: String,
                                           @Query("method") method: String = NavigationServiceConfig.GET_PACKET_CONTENT_WITH_FLAT_NAVIGATION,
                                           @Query("id") id : Int = 1,
                                           @Query("params") params: JsonRPCParams): Observable<GetPacketContentWithFlatNavigationResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_COMMONLY_ACCESSIBLE_PACKETS)
    fun getCommonlyAccessiblePackets(@Url url: String,
                                     @Query("method") method: String = NavigationServiceConfig.GET_COMMONLY_ACCESSIBLE_PACKETS,
                                     @Query("id") id : Int = 1,
                                     @Query("params") params: JsonRPCParams): Observable<List<CommonlyAccessiblePacketResult>>


    // Live
    @GET
    @JsonRPC(NavigationServiceConfig.GET_LIVE_CHANNELS)
    fun getLiveChannels(@Url url: String,
                        @Query("method") method: String = NavigationServiceConfig.GET_LIVE_CHANNELS,
                        @Query("id") id : Int = 1,
                        @Query("params") params: JsonRPCParams): Observable<GetLiveChannelsResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_LIVE_CHANNELS_WITH_TREE_NAVIGATION)
    fun getLiveChannelsWithTreeNavigation(@Url url: String,
                                          @Query("method") method: String = NavigationServiceConfig.GET_LIVE_CHANNELS_WITH_TREE_NAVIGATION,
                                          @Query("id") id : Int = 1,
                                          @Query("params") params: JsonRPCParams): Observable<GetLiveChannelsWithTreeNavigationResult>


    // Tv Channel
    @GET
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNELS_FLAT_NAVIGATION)
    fun getTvChannelsFlatNavigation(@Url url: String,
                                    @Query("method") method: String = NavigationServiceConfig.GET_TV_CHANNELS_FLAT_NAVIGATION,
                                    @Query("id") id : Int = 1,
                                    @Query("params") params: JsonRPCParams): Observable<GetTvChannelsFlatNavigationResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNELS_TREE_NAVIGATION)
    fun getTvChannelsTreeNavigation(@Url url: String,
                                    @Query("method") method: String = NavigationServiceConfig.GET_TV_CHANNELS_TREE_NAVIGATION,
                                    @Query("id") id : Int = 1,
                                    @Query("params") params: JsonRPCParams): Observable<GetTvChannelsTreeNavigationResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNELS)
    fun getTvChannels(@Url url: String,
                      @Query("method") method: String = NavigationServiceConfig.GET_TV_CHANNELS,
                      @Query("id") id : Int = 1,
                      @Query("params") params: JsonRPCParams): Observable<Result<GetTvChannelsResult>>


    // Tv Channel Program
    @GET
    @JsonRPC(NavigationServiceConfig.GET_CHANNELS_CURRENT_PROGRAM)
    fun getChannelsCurrentProgram(@Url url: String,
                                  @Query("method") method: String = NavigationServiceConfig.GET_CHANNELS_CURRENT_PROGRAM,
                                  @Query("id") id : Int = 1,
                                  @Query("params") params: JsonRPCParams): Observable<Result<Map<String, List<ChannelProgramItemResult>>>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_CHANNELS_PROGRAM)
    fun getChannelsProgram(@Url url: String,
                           @Query("method") method: String = NavigationServiceConfig.GET_CHANNELS_PROGRAM,
                           @Query("id") id : Int = 1,
                           @Query("params") params: JsonRPCParams): Observable<Result<Map<String, List<ChannelProgramItemResult>>>>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_TV_CHANNEL_PROGRAM_ITEM)
    fun getTvChannelProgramItem(@Url url: String,
                                @Query("method") method: String = NavigationServiceConfig.GET_TV_CHANNEL_PROGRAM_ITEM,
                                @Query("id") id : Int = 1,
                                @Query("params") params: JsonRPCParams): Observable<ChannelProgramItemResult>

    // Favorites
    @GET
    @JsonRPC(NavigationServiceConfig.GET_FAVORITES_TREE_NAVIGATION)
    fun getFavoritesTreeNavigation(@Url url: String,
                                   @Query("method") method: String = NavigationServiceConfig.GET_FAVORITES_TREE_NAVIGATION,
                                   @Query("id") id : Int = 1,
                                   @Query("params") params: JsonRPCParams): Observable<GetFavoritesTreeNavigationResult>

    @GET
    @JsonRPC(NavigationServiceConfig.GET_FAVORITES_WITH_FLAT_NAVIGATION)
    fun getFavoritesWithFlatNavigation(@Url url: String,
                                       @Query("method") method: String = NavigationServiceConfig.GET_FAVORITES_WITH_FLAT_NAVIGATION,
                                       @Query("id") id : Int = 1,
                                       @Query("params") params: JsonRPCParams): Observable<GetFavoritesWithFlatNavigationResult>

}
