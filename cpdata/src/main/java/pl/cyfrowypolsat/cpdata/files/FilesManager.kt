package pl.cyfrowypolsat.cpdata.files

import okhttp3.ResponseBody
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class FilesManager {

    @CpDataQualifier
    @Inject
    lateinit var filesApi: FilesApi

    init {
        CpData.getInstance().component.inject(this)
    }

    fun startDownload(url: String, localFilePath: String) {
        Timber.d("startDownload: $url localFilePath: $localFilePath")

        val call: Call<ResponseBody> = filesApi.downloadFile(url)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    saveFile(response.body(), localFilePath)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
            }
        })
    }

    fun removeFile(pathToFile: String): Boolean {
        return try {
            val file = File(pathToFile)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (t: Throwable) {
            false
        }
    }

    private fun saveFile(body: ResponseBody?, pathToFile: String) {
        Timber.d(body?.toString())
        var input: InputStream? = null
        try {
            input = body!!.byteStream()
            val fos = FileOutputStream(pathToFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }

            Timber.d(fos.toString())
        } catch (t: Throwable) {
            Timber.e(t)
        } finally {
            input?.close()
        }
    }
}