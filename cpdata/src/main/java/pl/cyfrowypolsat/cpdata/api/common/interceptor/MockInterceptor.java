package pl.cyfrowypolsat.cpdata.api.common.interceptor;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import pl.cyfrowypolsat.cpdata.BuildConfig;


public class MockInterceptor implements Interceptor {

    private final Context context;

    public MockInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (BuildConfig.DEBUG) {
            response = getErrorResponse(chain);
//            String requestBody = getRequestString(chain.request());
//            if (requestBody.contains("\"getProduct\"")) {
//                response = getMockResponse(chain, "mock/getProduct.json");
//            }
        }

        return response;
    }

    private Response getErrorResponse(Chain chain) {
        return new Response.Builder()
                .code(505)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), "error"))
                .addHeader("content-type", "application/json")
                .build();
    }

    private Response getMockResponse(Chain chain, String pathToMockAsset) {
        String responseString = getMockResponseFromAssets(pathToMockAsset);
        return new Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
    }

    private String getMockResponseFromAssets(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            InputStream json = context.getAssets().open(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                sb.append(str);
            }

            in.close();

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String getRequestString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return null;
        }
    }

}