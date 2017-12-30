package org.luftbild4p3d.bing

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class IntExtensionsTest : StringSpec ({

    "clippedTo returns minimum value if value is below range" {
        val actual = (-1).clippedTo(0, 2047)

        actual shouldEqual 0
    }

    "clippedTo returns maximum value if value above range" {
        val actual = (2048).clippedTo(0, 2047)

        actual shouldEqual 2047
    }

})