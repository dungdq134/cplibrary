package pl.cyfrowypolsat.cpdata.api.navigation

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.extensions.handleError
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.HeaderResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.ChannelProgramItemResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CommonlyAccessiblePacketResult
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.*
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetChannelsCurrentProgramParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetChannelsProgramParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetTvChannelProgramItemParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.favorites.GetFavoritesTreeNavigationParams
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
import pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation.Place
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
import pl.cyfrowypolsat.cpdata.api.system.response.Service
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import javax.inject.Inject


class NavigationService(private val configurationRepository: ConfigurationRepository) {

    @CpDataQualifier
    @Inject lateinit var navigationApi: NavigationApi

    @CpDataQualifier
    @Inject lateinit var navigationGetApi: NavigationGetApi

    init {
        CpData.getInstance().component.inject(this)
    }


    // Home Menu
    fun getHomeMenu(): Observable<List<HomeMenuItemResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getHomeMenu.firstVersion
                    runApi(service,
                            { navigationGetApi.getHomeMenu(it, params = JsonRPCParams()) },
                            { navigationApi.getHomeMenu(it, JsonRPCParams()) })
                }
    }


    // PrePlayData
    fun prePlayData(params: PrePlayDataParams): Observable<PrePlayDataResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.prePlayData.firstVersion
                    runApi(service,
                            { navigationGetApi.prePlayData(it, params = params) },
                            { navigationApi.prePlayData(it, params) })
                }
    }


    // Product
    fun getProduct(productParams: ProductParams): Observable<ProductResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getProduct.firstVersion
                    runApi(service,
                            { navigationGetApi.getProduct(it, params = productParams) },
                            { navigationApi.getProduct(it, productParams) })
                }
    }

    fun getProducts(productsParams: ProductsParams): Observable<List<ProductResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getProducts.firstVersion
                    runApi(service,
                            { navigationGetApi.getProducts(it, params = productsParams) },
                            { navigationApi.getProducts(it, productsParams) })
                }
    }

    fun getPurchaseCodeProduct(getPurchaseCodeProductParams: GetPurchaseCodeProductParams): Observable<ProductResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getPurchaseCodeProduct.firstVersion
                    runApi(service,
                            { navigationGetApi.getPurchaseCodeProduct(it, params = getPurchaseCodeProductParams) },
                            { navigationApi.getPurchaseCodeProduct(it, getPurchaseCodeProductParams) })
                }
    }

    fun getMultipleProducts(subtype: String): Observable<List<ProductResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getMultipleProducts.firstVersion
                    runApi(service,
                            { navigationGetApi.getMultipleProducts(it, params = GetMultipleProductsParams(subtype)) },
                            { navigationApi.getMultipleProducts(it, GetMultipleProductsParams(subtype)) })
                }
    }

    fun getBundles(bundleIds: List<String>): Observable<List<BundleResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getBundles.firstVersion
                    runApi(service,
                            { navigationGetApi.getBundles(it, params = GetBundlesParams(bundleIds)) },
                            { navigationApi.getBundles(it, GetBundlesParams(bundleIds)) })
                }
    }

    fun getOffer(offerParams: OfferParams): Observable<OfferResult> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val service = configurationResponse.services.navigation.getOffer.firstVersion
                runApi(service,
                        { navigationGetApi.getOffer(it, params = offerParams) },
                        { navigationApi.getOffer(it, offerParams) })
            }
    }


    // Media
    fun getMedia(getMediaParams: GetMediaParams): Observable<GetMediaResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getMedia.firstVersion
                    runApi(service,
                            { navigationGetApi.getMedia(it, params = getMediaParams) },
                            { navigationApi.getMedia(it, getMediaParams) })
                }
    }

    fun getMediaList(getMediaListParams: GetMediaListParams): Observable<List<GetMediaResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getMediaList.firstVersion
                    runApi(service,
                            { navigationGetApi.getMediaList(it, params = getMediaListParams) },
                            { navigationApi.getMediaList(it, getMediaListParams) })
                }
    }

    fun getMediaRelatedContent(params: GetMediaRelatedContentParams): Observable<GetMediaRelatedContentResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getMediaRelatedContent.firstVersion
                    runApi(service,
                            { navigationGetApi.getMediaRelatedContent(it, params = params) },
                            { navigationApi.getMediaRelatedContent(it, params) })
                }
    }


    // Category
    fun getCategory(params: GetCategoryParams): Observable<GetCategoryResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getCategory.firstVersion
                    runApi(service,
                            { navigationGetApi.getCategory(it, params = params) },
                            { navigationApi.getCategory(it, params) })
                }
    }

    fun getSubCategoriesWithBasicNavigation(catid: Int): Observable<List<CategoryWithBasicNavigationResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getSubCategoriesWithBasicNavigation.firstVersion
                    runApi(service,
                            { navigationGetApi.getSubCategoriesWithBasicNavigation(it, params = GetSubCategoriesWithBasicNavigationParams(catid)) },
                            { navigationApi.getSubCategoriesWithBasicNavigation(it, GetSubCategoriesWithBasicNavigationParams(catid)) })
                }
    }

    fun getCategoryWithFlatNavigation(catid: Int): Observable<CategoryWithFlatNavigationResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val params = GetCategoryWithFlatNavigationParams(catid)
                    val service = configurationResponse.services.navigation.getCategoryWithFlatNavigation.firstVersion
                    runApi(service,
                            { navigationGetApi.getCategoryWithFlatNavigation(it, params = params) },
                            { navigationApi.getCategoryWithFlatNavigation(it, params) })
                }
    }

    fun getCategoryContent(params: GetCategoryContentParams): Observable<GetCategoryContentResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getCategoryContent.firstVersion
                    runApi(service,
                            { navigationGetApi.getCategoryContent(it, params = params) },
                            { navigationApi.getCategoryContent(it, params) })
                }
    }

    fun getSubCategories(catid: Int): Observable<List<CategoryResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getSubCategories.firstVersion
                    runApi(service,
                            { navigationGetApi.getSubCategories(it, params = GetSubCategoriesParams(catid)) },
                            { navigationApi.getSubCategories(it, GetSubCategoriesParams(catid)) })
                }
    }

    fun getCategoryContentAutoplayMediaId(catid: Int): Observable<GetCategoryContentAutoplayMediaIdResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getCategoryContentAutoplayMediaId.firstVersion
                    runApi(service,
                            { navigationGetApi.getCategoryContentAutoplayMediaId(it, params = GetCategoryContentAutoplayMediaIdParams(catid)) },
                            { navigationApi.getCategoryContentAutoplayMediaId(it, GetCategoryContentAutoplayMediaIdParams(catid)) })
                }
    }


    // Recommendation
    fun getStaffRecommendationsLists(place: Place): Observable<List<GetStaffRecommendationsListResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getStaffRecommendationsLists.firstVersion
                    runApi(service,
                            { navigationGetApi.getStaffRecommendationsLists(it, params = GetStaffRecommendationsListsParams(place)) },
                            { navigationApi.getStaffRecommendationsLists(it, GetStaffRecommendationsListsParams(place)) })
                }
    }

    fun getStaffRecommendationsListItems(params: GetStaffRecommendationsListItemsParams): Observable<GetStaffRecommendationsListItemsResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getStaffRecommendationsListItems.firstVersion
                    runApi(service,
                            { navigationGetApi.getStaffRecommendationsListItems(it, params = params) },
                            { navigationApi.getStaffRecommendationsListItems(it, params) })
                }
    }


    // Search
    fun searchContent(params: SearchParams): Observable<SearchContentResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.searchContent.firstVersion
                    runApi(service,
                            { navigationGetApi.searchContent(it, params = params) },
                            { navigationApi.searchContent(it, params) })
                }
    }


    // Packet
    fun getPacketTreeNavigation(params: GetPacketTreeNavigationParams): Observable<GetPacketTreeNavigationResult> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val service = configurationResponse.services.navigation.getPacketTreeNavigation.firstVersion
                runApi(service,
                        { navigationGetApi.getPacketTreeNavigation(it, params = params) },
                        { navigationApi.getPacketTreeNavigation(it, params) })
            }
    }

    fun getPacketContent(params: GetPacketContentParams): Observable<GetPacketContentResult> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val service = configurationResponse.services.navigation.getPacketContent.firstVersion
                runApi(service,
                        { navigationGetApi.getPacketContent(it, params = params) },
                        { navigationApi.getPacketContent(it, params) })
            }
    }

    fun getPacketContentWithFlatNavigation(params: GetPacketContentWithFlatNavigationParams): Observable<GetPacketContentWithFlatNavigationResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getPacketContentWithFlatNavigation.firstVersion
                    runApi(service,
                            { navigationGetApi.getPacketContentWithFlatNavigation(it, params = params) },
                            { navigationApi.getPacketContentWithFlatNavigation(it, params) })
                }
    }

    fun getCommonlyAccessiblePackets(): Observable<List<CommonlyAccessiblePacketResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getCommonlyAccessiblePackets.firstVersion
                    runApi(service,
                            { navigationGetApi.getCommonlyAccessiblePackets(it, params = JsonRPCParams()) },
                            { navigationApi.getCommonlyAccessiblePackets(it, JsonRPCParams()) })
                }
    }

    // Live
    fun getLiveChannels(params: GetLiveChannelsParams): Observable<GetLiveChannelsResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getLiveChannels.firstVersion
                    runApi(service,
                            { navigationGetApi.getLiveChannels(it, params = params) },
                            { navigationApi.getLiveChannels(it, params) })
                }
    }

    fun getLiveChannelsWithTreeNavigation(params: GetLiveChannelsWithTreeNavigationParams): Observable<GetLiveChannelsWithTreeNavigationResult>{
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    runApi(configurationResponse.services.navigation.getLiveChannelsWithTreeNavigation.firstVersion,
                            { navigationGetApi.getLiveChannelsWithTreeNavigation(it, params = params) },
                            { navigationApi.getLiveChannelsWithTreeNavigation(it, params) })
                }
    }


    // Tv Channel
    fun getTvChannelsFlatNavigation(): Observable<GetTvChannelsFlatNavigationResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getTvChannelsFlatNavigation.firstVersion
                    runApi(service,
                            { navigationGetApi.getTvChannelsFlatNavigation(it, params = JsonRPCParams()) },
                            { navigationApi.getTvChannelsFlatNavigation(it, JsonRPCParams()) })
                }
    }

    fun getTvChannelsTreeNavigation(): Observable<GetTvChannelsTreeNavigationResult> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val service = configurationResponse.services.navigation.getTvChannelsTreeNavigation.firstVersion
                runApi(service,
                        { navigationGetApi.getTvChannelsTreeNavigation(it, params = JsonRPCParams()) },
                        { navigationApi.getTvChannelsTreeNavigation(it, JsonRPCParams()) })
            }
    }

    fun getTvChannels(params: GetTvChannelsParams): Observable<Pair<HeaderResult?, GetTvChannelsResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getTvChannels.firstVersion
                    runApi(service,
                            { navigationGetApi.getTvChannels(it, params = params) },
                            { navigationApi.getTvChannels(it, params) })
                            .map {
                                it.handleError()
                                Pair(HeaderResult.from(it.response()?.headers()), it.response()?.body()!!)
                            }
                }
    }


    // Tv Channel Program
    fun getChannelsCurrentProgram(params: GetChannelsCurrentProgramParams): Observable<Map<String, List<ChannelProgramItemResult>>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getChannelsCurrentProgram.firstVersion
                    runApi(service,
                            { navigationGetApi.getChannelsCurrentProgram(it, params = params) },
                            { navigationApi.getChannelsCurrentProgram(it, params) })
                            .map {
                                it.handleError()
                                it.response()?.body()!!
                            }
                }
    }

    fun getChannelsProgram(params: GetChannelsProgramParams): Observable<Pair<HeaderResult?, Map<String, List<ChannelProgramItemResult>>>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getChannelsProgram.firstVersion
                    runApi(service,
                            { navigationGetApi.getChannelsProgram(it, params = params) },
                            { navigationApi.getChannelsProgram(it, params) })
                            .map {
                                it.handleError()
                                Pair(HeaderResult.from(it.response()?.headers()), it.response()?.body()!!)
                            }
                }
    }

    fun getTvChannelProgramItem(params: GetTvChannelProgramItemParams):  Observable<ChannelProgramItemResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getTvChannelProgramItem.firstVersion
                    runApi(service,
                            { navigationGetApi.getTvChannelProgramItem(it, params = params) },
                            { navigationApi.getTvChannelProgramItem(it, params) })
                }
    }

    // Favorites
    fun getFavoritesTreeNavigation(params: GetFavoritesTreeNavigationParams):  Observable<GetFavoritesTreeNavigationResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getFavoritesTreeNavigation.firstVersion
                    runApi(service,
                            { navigationGetApi.getFavoritesTreeNavigation(it, params = params) },
                            { navigationApi.getFavoritesTreeNavigation(it, params) })
                }
    }

    fun getFavoritesWithFlatNavigation(params: GetFavoritesWithFlatNavigationParams): Observable<GetFavoritesWithFlatNavigationResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val service = configurationResponse.services.navigation.getFavoritesWithFlatNavigation.firstVersion
                    runApi(service,
                            { navigationGetApi.getFavoritesWithFlatNavigation(it, params = params) },
                            { navigationApi.getFavoritesWithFlatNavigation(it, params) })
                }
    }

}

private fun <T> NavigationService.runApi(service: Service, getApi: (url: String) -> T, api: (url: String) -> T): T {
    return service.getUrl?.let(getApi) ?: api(service.url)
}