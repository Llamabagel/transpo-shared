/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import ca.llamabagel.transpo.models.app.Version
import org.junit.Assert.*
import org.junit.Test

class VersionTest {

    @Test
    fun testVersionComparison() {
        val versionA = Version("20190123")
        val versionB = Version("20190311")
        val versionC = Version("20190311-2")

        assertTrue(versionA < versionB)
        assertTrue(versionB < versionC)
        assertTrue(versionA < versionC)
    }

}
