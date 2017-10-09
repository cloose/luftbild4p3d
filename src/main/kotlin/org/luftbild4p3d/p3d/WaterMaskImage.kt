package org.luftbild4p3d.app

import org.luftbild4p3d.bing.*
import org.luftbild4p3d.osm.NodeReference
import org.luftbild4p3d.osm.Osm
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Polygon
import java.awt.geom.Path2D
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Math.*
import javax.imageio.ImageIO

data class Point(val x: Int, val y: Int)
data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

class WaterMaskImage(val tile: Tile, val osm: Osm, val maskSize: Int) {

    val maskImage = BufferedImage(maskSize, maskSize, BufferedImage.TYPE_BYTE_GRAY)
    val graphics = maskImage.createGraphics()
    val tileOffset = tile.toPixelCoordinates()

    val leftBorder = Line(0, 0, 0, maskSize-1)
    val rightBorder = Line(maskSize-1, 0, maskSize-1, maskSize-1)
    val topBorder = Line(0, 0, maskSize-1, 0)
    val bottomBorder = Line(0, maskSize-1, maskSize-1, maskSize-1)

    init {
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, maskSize, maskSize)
        graphics.stroke = BasicStroke(2.0f)

        drawRelations()
        //drawCoastlines()
        drawWays()
    }

    public fun writeToFile(fileName: String) {
        ImageIO.write(maskImage.getSubimage(500, 500, 4096, 4096), "tiff", File(fileName))
    }

    private fun drawRelations() {
        osm.relation.forEach {
            it.member.forEach { member ->
                val polygon = Polygon()
                member.nodeReference.forEachIndexed { j, nodeRef ->
                    addNodeToPolygon(member.nodeReference.toList(),j, polygon)
                }

                graphics.color = if (member.role.equals("inner")) { println("**INNER**")
                    Color.RED } else Color.BLACK
                println("Color is: " + graphics.color + "and polygon has" + polygon.npoints)
                graphics.fillPolygon(polygon)
            }
        }
    }

    private fun drawCoastlines() {
        val coastlines = osm.way.filter {it.tag.any { tag -> tag.value.equals("coastline") }  }

        if (coastlines.isNotEmpty()) {
            var polygon = Polygon()

            val it = coastlines.listIterator()
            var coastline = it.next()
            coastline.nodeReference.forEachIndexed{ ix, nodeRef ->
                addNodeToPolygon(coastline.nodeReference.toList(), ix, polygon)
            }

            while (it.hasNext()) {
                val currentCoastline = it.next()

                if (currentCoastline.nodeReference.first().ref != coastline.nodeReference.last().ref) {
                    graphics.color = Color.BLACK
                    graphics.fillPolygon(polygon)
                    polygon = Polygon()
                }

                currentCoastline.nodeReference.forEachIndexed{ ix, nodeRef ->
                    addNodeToPolygon(currentCoastline.nodeReference.toList(), ix, polygon)
                }

                coastline = currentCoastline
            }

            graphics.color = Color.BLACK
            graphics.fillPolygon(polygon)
        }

        println(coastlines.size)
    }

    var inside = false
    private fun drawWays() {
        inside = false
        osm.way.filter { it.tag.all { tag -> !tag.value.equals("coastline") } }.forEach { way ->
            println("Process way ${way.id} which is closed? ${way.closed()}")
            val polygon = Polygon()
            way.nodeReference.forEachIndexed { ix, nodeRef ->
                addNodeToPolygon(way.nodeReference.toList(), ix, polygon)
            }
            graphics.color = Color.BLACK
            graphics.fillPolygon(polygon)
        }
    }

    private fun addNodeToPolygon(nodeRefList: List<NodeReference>, index: Int, polygon: Polygon) {
        val nodeRef = nodeRefList[index]
        val (x, y, _) = Wgs84Coordinates(nodeRef!!.latitude.toDouble(), nodeRef!!.longitude.toDouble()).toPixelCoordinates(tile.levelOfDetail)
        val x2 = x - tileOffset.x + 500
        val y2 = y - tileOffset.y + 500

        if ((x2 >= 0) and (x2 < maskSize) and (y2 >= 0) and (y2 < maskSize)) {
            println("Point ($x2, $y2) inside of image.")

            if ((index > 0) and (!inside)) {
                val previousNodeRef = nodeRefList[index - 1]
                val (xp, yp, _) = Wgs84Coordinates(previousNodeRef!!.latitude.toDouble(), previousNodeRef!!.longitude.toDouble()).toPixelCoordinates(tile.levelOfDetail)
                val line = Line(xp - tileOffset.x, yp - tileOffset.y, x2, y2)
                val (xs, ys) = calculateIntersection(line, rightBorder)
                if ((xs >= 0) and (xs < maskSize) and (ys >= 0) and (ys < maskSize)) {
                    println("Previous intersects right border at ($xs, $ys)")
                    polygon.addPoint(xs, ys)
                } else {
                    val (xsl, ysl) = calculateIntersection(line, leftBorder)
                    if ((xsl >= 0) and (xsl < maskSize) and (ysl >= 0) and (ysl < maskSize)) {
                        println("Previous intersects left border at ($xsl, $ysl)")
                        polygon.addPoint(xsl, ysl)
                    } else {
                        val (xst, yst) = calculateIntersection(line, topBorder)
                        if ((xst >= 0) and (xst < maskSize) and (yst >= 0) and (yst < maskSize)) {
                            println("Previous intersects top border at ($xst, $yst)")
                            polygon.addPoint(xst, yst)
                        } else {
                            val (xsb, ysb) = calculateIntersection(line, bottomBorder)
                            if ((xsb >= 0) and (xsb < maskSize) and (ysb >= 0) and (ysb < maskSize)) {
                                println("Previous intersects bottom border at ($xsb, $ysb)")
                                polygon.addPoint(xsb, ysb)
                            }
                        }
                    }
                }
            }

            inside = true
            polygon.addPoint(x2, y2)
        } else {
            print("Point ($x2, $y2) outside of image.")
            if (polygon.npoints > 0) {
                val xoutside = (x2 < 0) or (x2 >= maskSize)
                val youtside = (y2 < 0) or (y2 >= maskSize)
                val xdiff = if (xoutside) if (x2 < 0) abs(x2 - 0) else abs(x2 - maskSize) else 0
                val ydiff = if (youtside) if (y2 < 0) abs(y2 - 0) else abs(y2 - maskSize) else 0
                val toofar = (xdiff > 100) or (ydiff > 100)

                val xp = polygon.xpoints[polygon.npoints - 1]
                val yp = polygon.ypoints[polygon.npoints - 1]
                println("Previous point was ($xp, $yp). Too far? $toofar ($xdiff, $ydiff)")
                val line = Line(xp, yp, x2, y2)

                if (toofar) return

                val (xs, ys) = calculateIntersection(line, rightBorder)
                if ((xs >= 0) and (xs < maskSize) and (ys >= 0) and (ys < maskSize)) {
                    println("Intersects right border at ($xs, $ys)")
                    polygon.addPoint(xs, ys)
                } else {
                    val (xsl, ysl) = calculateIntersection(line, leftBorder)
                    if ((xsl >= 0) and (xsl < maskSize) and (ysl >= 0) and (ysl < maskSize)) {
                        println("Intersects left border at ($xsl, $ysl)")
                        polygon.addPoint(xsl, ysl)
                    } else {
                        val (xst, yst) = calculateIntersection(line, topBorder)
                        if ((xst >= 0) and (xst < maskSize) and (yst >= 0) and (yst < maskSize)) {
                            println("Intersects top border at ($xst, $yst)")
                            polygon.addPoint(xst, yst)
                        } else {
                            val (xsb, ysb) = calculateIntersection(line, bottomBorder)
                            if ((xsb >= 0) and (xsb < maskSize) and (ysb >= 0) and (ysb < maskSize)) {
                                println("Intersects bottom border at ($xsb, $ysb)")
                                polygon.addPoint(xsb, ysb)
                            }
                        }
                    }
                }
                inside = false
            } else println()
        }
    }


    private fun calculateIntersection(line1: Line, line2: Line): Point {
        val s10_x = line1.x2 - line1.x1
        val s10_y = line1.y2 - line1.y1
        val s32_x = line2.x2 - line2.x1
        val s32_y = line2.y2 - line2.y1

        val denom = s10_x * s32_y - s32_x * s10_y

        if (denom == 0) {
            println("Collinear (denom == 0)")
            return Point(-1, -1)
        } // collinear

        val denom_is_positive = denom > 0

        val s02_x = line1.x1 - line2.x1
        val s02_y = line1.y1 - line2.y1

        val s_numer = s10_x * s02_y - s10_y * s02_x

        if ((s_numer < 0) == denom_is_positive) {
            println("No Collision ($s_numer < 0 == denom_is_positive)")
            return Point(-1, -1)
        } // no collision

        val t_numer = s32_x * s02_y - s32_y * s02_x

        if ((t_numer < 0) == denom_is_positive) {
            println("No Collision ($t_numer < 0 == denom_is_positive)")
            return Point(-1, -1)
        } // no collision

        val a = (s_numer > denom) == denom_is_positive
        val b = (t_numer > denom) == denom_is_positive
        if (a or b) {
            println("No Collision ($s_numer > $denom or $t_numer > $denom == denom_is_positive)")
            return Point(-1, -1)
        } // no collision

        // collision detected

        val t = t_numer.toDouble() / denom.toDouble()

        val intersection_point = Point(line1.x1 + (t * s10_x).toInt(), line1.y1 + (t * s10_y).toInt())
        println("Intersects at ${intersection_point.x},${intersection_point.y} with $t = $t_numer / $denom")
        return intersection_point
    }

}
