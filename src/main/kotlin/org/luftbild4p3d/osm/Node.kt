package org.luftbild4p3d.osm

import java.math.BigDecimal
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.FIELD)
class Node(@XmlAttribute val id: Long = 0, @XmlAttribute(name ="lat") val latitude: BigDecimal = BigDecimal.ZERO, @XmlAttribute(name ="lon") val longitude: BigDecimal = BigDecimal.ZERO)  {
}