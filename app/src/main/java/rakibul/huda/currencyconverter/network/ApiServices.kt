package rakibul.huda.currencyconverter.network

import rakibul.huda.currencyconverter.model.JsonVatResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {

    @get:GET("/")
    val vats: Call<JsonVatResponse>

}
