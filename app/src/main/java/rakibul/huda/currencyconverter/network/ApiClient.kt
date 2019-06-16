package rakibul.huda.currencyconverter.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val BASE_URL = "https://jsonvat.com/"
    private var retrofit: Retrofit? = null


    val client: Retrofit?
        get() {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build()

                retrofit = Retrofit.Builder()
                        .client(client)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }

}
