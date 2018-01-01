package org.luftbild4p3d.osm.types

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
data class Member(@field:XmlAttribute val type: String = "",
                  @field:XmlAttribute val ref: Long = 0L,
                  @field:XmlAttribute val role: String = "",
                  @field:XmlElement(name = "nd") val nodeReference: MutableList<NodeReference> = mutableListOf())
