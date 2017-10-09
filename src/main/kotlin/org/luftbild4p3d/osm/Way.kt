package org.luftbild4p3d.osm

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

class Way(@XmlAttribute val id: Long = 0) {

    @XmlElement(name = "nd")
    val nodeReference: MutableCollection<NodeReference> = mutableListOf()

    @XmlElement(name = "tag")
    val tag: MutableCollection<Tag> = mutableListOf()

    fun closed() =  nodeReference.first().ref == nodeReference.last().ref
}