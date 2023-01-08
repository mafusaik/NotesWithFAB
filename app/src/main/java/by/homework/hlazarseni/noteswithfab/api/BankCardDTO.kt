package by.homework.hlazarseni.noteswithfab.api

data class BankCardDTO(
    val number: NumberDTO?,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean?,
    val country: CountryDTO?,
    val bank: BankDTO?
)

data class NumberDTO(
    val length: String?,
    val luhn: Boolean?
)

data class CountryDTO(
    val numeric: String?,
    val alpha2: String?,
    val name: String?,
    val emoji: String?,
    val currency: String?,
    val latitude: String?,
    val longitude: String?,
)

data class BankDTO(
    val name: String?,
    val url: String?,
    val phone: String?,
    val city: String?
)