package pl.cyfrowypolsat.cpdata.api.navigation.request.common

data class InputFilter(
        val name: String = "",
        val type: String,
        val value: List<String>
)

data class SingleValueInputFilter(
        val name: String = "",
        val type: String,
        val value: String
)