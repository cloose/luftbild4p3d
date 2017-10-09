package org.luftbild4p3d.osm

import javax.xml.bind.annotation.XmlAttribute

class Tag(@XmlAttribute(name ="k") val key: String = "", @XmlAttribute(name = "v") val value: String = "") {
}