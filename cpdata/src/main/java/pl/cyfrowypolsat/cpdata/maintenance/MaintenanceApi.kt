package pl.cyfrowypolsat.cpdata.maintenance

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.maintenance.model.MaintenanceResult
import retrofit2.http.GET
import retrofit2.http.Url

interface MaintenanceApi {
    @GET
    fun getMaintenanceData(@Url url: String): Observable<MaintenanceResult>
}