package org.luftbild4p3d.bing

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class DoubleExtensionsTest : StringSpec ({

    "clippedTo returns minimum value if value is below range" {
        val actual = (-0.1).clippedTo(0.0, 2047.0)

        actual shouldEqual 0.0
    }

    "clippedTo returns maximum value if value above range" {
        val actual = (2047.1).clippedTo(0.0, 2047.0)

        actual shouldEqual 2047.0
    }

})