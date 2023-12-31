package pl.cyfrowypolsat.cpdata.files

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FilesApi {
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>
}