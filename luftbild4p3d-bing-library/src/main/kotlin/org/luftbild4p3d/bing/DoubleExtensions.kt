package org.luftbild4p3d.bing

fun Double.clippedTo(minimumValue: Double, maximumValue: Double) = Math.min(Math.max(this, minimumValue), maximumValue)
