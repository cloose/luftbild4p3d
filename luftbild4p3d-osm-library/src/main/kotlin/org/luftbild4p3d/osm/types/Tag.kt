package org.luftbild4p3d.osm.types

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.FIELD)
data class Tag(@field:XmlAttribute(name = "k") val key: String = "",
               @field:XmlAttribute(name = "v") val value: String = "")