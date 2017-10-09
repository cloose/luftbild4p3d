package org.luftbild4p3d.osm

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

class Member(@XmlAttribute val type: String = "", @XmlAttribute val ref: Long = 0, @XmlAttribute val role: String = "") {

    @XmlElement(name = "nd")
    val nodeReference: MutableCollection<NodeReference> = mutableListOf()

}