package org.luftbild4p3d.osm.types

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
data class Way(@field:XmlAttribute val id: Long = 0L,
               @field:XmlElement(name = "nd") val nodeReference: MutableList<NodeReference> = mutableListOf(),
               @field:XmlElement val tag: MutableList<Tag> = mutableListOf())
