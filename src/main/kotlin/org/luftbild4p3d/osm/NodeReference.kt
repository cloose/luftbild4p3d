package org.luftbild4p3d.osm

import java.math.BigDecimal
import javax.xml.bind.annotation.XmlAttribute

class NodeReference(@XmlAttribute val ref: Long = 0, @XmlAttribute(name ="lat") val latitude: BigDecimal = BigDecimal.ZERO, @XmlAttribute(name ="lon") val longitude: BigDecimal = BigDecimal.ZERO) {
}