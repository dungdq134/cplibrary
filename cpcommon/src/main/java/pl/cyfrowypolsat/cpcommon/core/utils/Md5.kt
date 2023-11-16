package pl.cyfrowypolsat.cpcommon.core.utils

import java.security.MessageDigest

object Md5 {
    val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    fun fromString(s: String): String {
        val bytes = MessageDigest
                .getInstance("MD5")
                .digest(s.toByteArray())
        return printHexBinary(bytes).toUpperCase()
    }

    private fun printHexBinary(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }
}