package org.luftbild4p3d.bing

fun Int.clippedTo(minimumValue: Int, maximumValue: Int) = Math.min(Math.max(this, minimumValue), maximumValue)
