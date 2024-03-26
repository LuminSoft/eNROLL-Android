package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.countries_code

import com.google.gson.annotations.SerializedName

open class GetCountriesResponseModel {

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("name")
    internal var name: String? = null

    @SerializedName("code")
    internal var code: String? = null

    @SerializedName("countryISOCode")
    internal var countryISOCode: String? = null

    @SerializedName("phoneNumberRegex")
    internal var phoneNumberRegex: String? = null

}
