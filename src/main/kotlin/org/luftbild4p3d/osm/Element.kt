package org.luftbild4p3d.osm

import javax.xml.bind.annotation.XmlAttribute

abstract class Element(@XmlAttribute val id: Long = 0) {
}