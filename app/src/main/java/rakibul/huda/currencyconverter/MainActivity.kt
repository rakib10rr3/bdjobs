package rakibul.huda.currencyconverter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import rakibul.huda.currencyconverter.model.JsonVatResponse
import rakibul.huda.currencyconverter.model.Rate
import rakibul.huda.currencyconverter.network.ApiClient
import rakibul.huda.currencyconverter.network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    internal var mContext: Context? = null

    internal var rdobtnSelectedVatRate: RadioButton? = null

    internal var rateData: List<Rate>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        Logger.addLogAdapter(AndroidLogAdapter())

        mContext = this@MainActivity


        // Init views
        rdogrp_vat_rates.visibility = View.GONE


        // Call API
        val apiServices = ApiClient.client!!.create<ApiServices>(ApiServices::class.java)

        apiServices.vats.enqueue(object : Callback<JsonVatResponse> {
            override fun onResponse(call: Call<JsonVatResponse>, response: Response<JsonVatResponse>) {

                rateData = response.body()!!.rates

                loadCountries(rateData)

            }

            override fun onFailure(call: Call<JsonVatResponse>, t: Throwable) {

            }
        })


        // Country Dropdown Listener
        tiet_currency_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                try {

                    tv_original_amount.text = String.format(Locale.ENGLISH, "%.1f", java.lang.Double.parseDouble(s.toString()))

                    calculateTotal()

                } catch (e: NumberFormatException) {

                    tv_original_amount.text = "0.0"

                }

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        actv_countries.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            loadVatRates(position)

            rdogrp_vat_rates.isEnabled = true

            rdogrp_vat_rates.visibility = View.VISIBLE
        }

        rdogrp_vat_rates.setOnCheckedChangeListener { group, checkedId ->
            rdobtnSelectedVatRate = findViewById(checkedId)

            calculateTotal()
        }

    }

    internal fun loadCountries(rates: List<Rate>?) {

        val countries = ArrayList<String>()

        if (rates != null) {
            for (rate in rates) {

                val name = rate.name
                if (name != null)
                    countries.add(name)
            }

            val countryAdapter = ArrayAdapter(mContext, R.layout.country_dropdown_menu_popup_item, countries)

            actv_countries.setAdapter(countryAdapter)
        }


    }

    internal fun loadVatRates(position: Int) {

        val rates = rateData?.get(position)?.periods?.get(0)?.rates

        var isSelected = false

        if (rates!!.superReduced != null) {
            rdobtn_super_reduced.text = "Super Reduced (" + rates.superReduced + "%)"
            rdobtn_super_reduced.visibility = View.VISIBLE
            rdobtn_super_reduced.tag = rates.superReduced
            if (!isSelected) {
                isSelected = true
                rdobtn_super_reduced.isChecked = true
            }
        } else {
            rdobtn_super_reduced.visibility = View.GONE
        }

        if (rates.reduced != null) {
            rdobtn_reduced.text = "Reduced (" + rates.reduced + "%)"
            rdobtn_reduced.visibility = View.VISIBLE
            rdobtn_reduced.tag = rates.reduced
            if (!isSelected) {
                isSelected = true
                rdobtn_reduced.isChecked = true
            }
        } else {
            rdobtn_reduced.visibility = View.GONE
        }

        if (rates.reduced1 != null) {
            rdobtn_reduced_1.text = "Reduced 1 (" + rates.reduced1 + "%)"
            rdobtn_reduced_1.visibility = View.VISIBLE
            rdobtn_reduced_1.tag = rates.reduced1
            if (!isSelected) {
                isSelected = true
                rdobtn_reduced_1.isChecked = true
            }
        } else {
            rdobtn_reduced_1.visibility = View.GONE
        }

        if (rates.reduced2 != null) {
            rdobtn_reduced_2.text = "Reduced 2 (" + rates.reduced2 + "%)"
            rdobtn_reduced_2.visibility = View.VISIBLE
            rdobtn_reduced_2.tag = rates.reduced2
            if (!isSelected) {
                isSelected = true
                rdobtn_reduced_2.isChecked = true
            }
        } else {
            rdobtn_reduced_2.visibility = View.GONE
        }

        if (rates.standard != null) {
            rdobtn_standard.text = "Standard (" + rates.standard + "%)"
            rdobtn_standard.visibility = View.VISIBLE
            rdobtn_standard.tag = rates.standard
            if (!isSelected) {
                isSelected = true
                rdobtn_standard.isChecked = true
            }
        } else {
            rdobtn_standard.visibility = View.GONE
        }

        if (rates.parking != null) {
            rdobtn_parking.text = "Parking (" + rates.parking + "%)"
            rdobtn_parking.visibility = View.VISIBLE
            rdobtn_parking.tag = rates.parking
            if (!isSelected) {
                isSelected = true
                rdobtn_parking.isChecked = true
            }
        } else {
            rdobtn_parking.visibility = View.GONE
        }

    }

    internal fun calculateTotal() {
        val currentVatRate = rdobtnSelectedVatRate!!.tag as Double

        try {

            val totalVatRate = java.lang.Double.parseDouble(tiet_currency_amount.text!!.toString()) * currentVatRate / 100

            tv_tax.text = String.format(Locale.ENGLISH, "%.1f", totalVatRate)

            tv_total.text = String.format(Locale.ENGLISH, "%.1f", totalVatRate + java.lang.Double.parseDouble(tiet_currency_amount.text!!.toString()))

        } catch (e: Exception) {

            tv_tax.text = "0.0"

        }

    }

    companion object {

        internal val TAG = MainActivity::class.java.simpleName
    }


}
