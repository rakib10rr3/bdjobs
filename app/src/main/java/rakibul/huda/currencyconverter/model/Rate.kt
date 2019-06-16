package rakibul.huda.currencyconverter.model

data class Rate (


    var name: String? = null,
    var code: String? = null,
    var countryCode: String? = null,
    var periods: List<Period>? = null


)
