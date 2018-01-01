package org.luftbild4p3d.osm.types

import java.math.BigDecimal
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.FIELD)
data class NodeReference(@field:XmlAttribute val ref: Long = 0L,
                         @field:XmlAttribute(name = "lat") val latitude: BigDecimal = BigDecimal.ZERO,
                         @field:XmlAttribute(name = "lon") val longitude: BigDecimal = BigDecimal.ZERO)