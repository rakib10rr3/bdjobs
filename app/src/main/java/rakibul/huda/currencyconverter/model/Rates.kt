package rakibul.huda.currencyconverter.model

import com.google.gson.annotations.SerializedName

data class Rates (

    @SerializedName("super_reduced")
    var superReduced: Double? = null,

    var reduced: Double? = null,

    var standard: Double? = null,

    var reduced1: Double? = null,

    var reduced2: Double? = null,

    var parking: Double? = null


)

