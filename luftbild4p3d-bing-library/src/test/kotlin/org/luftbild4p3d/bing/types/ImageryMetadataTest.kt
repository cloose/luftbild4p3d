package org.luftbild4p3d.bing.types

import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD14
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD18
import io.kotlintest.properties.*

class ImageryMetadataTest : StringSpec({

    "nextSubDomain returns a random element from list of image URL sub domains" {
        val imageUrlSubDomains = listOf("s1", "s2", "s3")
        val metadata = ImageryMetadata(256, 256, "http://example.com", imageUrlSubDomains, "")
        var actual = mutableListOf<String>()

        for (i in 1..10) {
            actual.add(nextSubDomain(metadata))

            actual.last() should elementOf(imageUrlSubDomains)
        }

        actual should containsAll(imageUrlSubDomains)
    }

})

private fun elementOf(strings: List<String>) = object : Matcher<String> {
    override fun test(value: String) = Result(strings.contains(value), "$value should be element of ${strings.joinToString()}")
}