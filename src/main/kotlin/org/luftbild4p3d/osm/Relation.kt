package org.luftbild4p3d.osm

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

class Relation(@XmlAttribute val id: Long = 0) {

    @XmlElement(name = "member")
    val member: MutableCollection<Member> = mutableListOf()

    @XmlElement(name = "tag")
    val tag: MutableCollection<Tag> = mutableListOf()

}