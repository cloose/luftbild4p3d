package org.luftbild4p3d.bing

import io.kotlintest.matchers.Matcher
import io.kotlintest.matchers.Result
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class ImageryMetadataTest : StringSpec({

    "numberOfSubdomains returns number of image URL sub domains in first resource set" {
        val imageUrlSubdomains = arrayOf("s1", "s2", "s3")
        val metadata = ImageryMetadata(resourceSets = arrayOf(ResourceSet(resources = arrayOf(Resource(imageUrlSubdomains = imageUrlSubdomains)))))

        val actual = metadata.numberOfSubdomains()

        actual shouldEqual imageUrlSubdomains.size
    }

    "nextSubDomain returns random image URL sub domain from first resource set" {
        val imageUrlSubdomains = arrayOf("s1", "s2", "s3")
        val metadata = ImageryMetadata(resourceSets = arrayOf(ResourceSet(resources = arrayOf(Resource(imageUrlSubdomains = imageUrlSubdomains)))))

        for (i in 1..10) {
            metadata.nextSubDomain() should elementOf(imageUrlSubdomains)
        }
    }

    "imageUrl returns image URL with filled sub domain from first resource set" {
        val imageUrlSubdomains = arrayOf("s1")
        val imageUrlTemplate = "http://ecn.{subdomain}.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=5907"
        val metadata = ImageryMetadata(resourceSets = arrayOf(ResourceSet(resources = arrayOf(Resource(imageUrl = imageUrlTemplate, imageUrlSubdomains = imageUrlSubdomains)))))

        val actual = metadata.imageUrl()

        actual shouldEqual "http://ecn.s1.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=5907"
    }

})

fun elementOf(strings: Array<String>) = object : Matcher<String> {
    override fun test(value: String) = Result(strings.contains(value), "$value should be element of ${strings.joinToString()}")
}