package pl.cyfrowypolsat.cpcommon

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.cyfrowypolsat.cpcommon.core.utils.Md5

class Md5Test {
    @Test
    fun fromStringTest() {
        val md5 = Md5.fromString("test")
        assertEquals("098F6BCD4621D373CADE4E832627B4F6", md5)
    }
}