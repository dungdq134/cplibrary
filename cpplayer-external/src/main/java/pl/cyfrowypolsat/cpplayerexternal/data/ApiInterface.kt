package pl.cyfrowypolsat.cpplayerexternal.data

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpplayerexternal.data.model.PrePlayData
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    @GET
    fun getPrePlayData(@Url url: String): Observable<PrePlayData>

}