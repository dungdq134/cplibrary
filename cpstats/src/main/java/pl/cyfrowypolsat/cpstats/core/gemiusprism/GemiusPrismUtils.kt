package pl.cyfrowypolsat.cpstats.core.gemiusprism

import android.net.Uri

internal object GemiusPrismUtils {
    fun specialEscape(input: String): String {
        var escapedString: String = input.replace(" ", "_")
        escapedString = escapedString.replace("=", "_")
        escapedString = escapedString.replace("&", "_")
        escapedString = escapedString.replace("+", "_")
        return escapedString
    }

    fun urlEncode(input: String): String {
        return Uri.encode(input)
    }
}