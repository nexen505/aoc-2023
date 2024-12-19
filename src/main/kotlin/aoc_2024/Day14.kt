package aoc_2024

import println
import readInput

/**
 * --- Day 14: Restroom Redoubt ---
 *
 * One of The Historians needs to use the bathroom; fortunately, you know there's a bathroom near an unvisited location on their list, and so you're all quickly teleported directly to the lobby of Easter Bunny Headquarters.
 *
 * Unfortunately, EBHQ seems to have "improved" bathroom security again after your last visit. The area outside the bathroom is swarming with robots!
 *
 * To get The Historian safely to the bathroom, you'll need a way to predict where the robots will be in the future. Fortunately, they all seem to be moving on the tile floor in predictable straight lines.
 *
 * You make a list (your puzzle input) of all of the robots' current positions (p) and velocities (v), one robot per line. For example:
 *
 * p=0,4 v=3,-3
 * p=6,3 v=-1,-3
 * p=10,3 v=-1,2
 * p=2,0 v=2,-1
 * p=0,0 v=1,3
 * p=3,0 v=-2,-2
 * p=7,6 v=-1,-3
 * p=3,0 v=-1,-2
 * p=9,3 v=2,3
 * p=7,3 v=-1,2
 * p=2,4 v=2,-3
 * p=9,5 v=-3,-3
 *
 * Each robot's position is given as p=x,y where x represents the number of tiles the robot is from the left wall and y represents the number of tiles from the top wall (when viewed from above). So, a position of p=0,0 means the robot is all the way in the top-left corner.
 *
 * Each robot's velocity is given as v=x,y where x and y are given in tiles per second. Positive x means the robot is moving to the right, and positive y means the robot is moving down. So, a velocity of v=1,-2 means that each second, the robot moves 1 tile to the right and 2 tiles up.
 *
 * The robots outside the actual bathroom are in a space which is 101 tiles wide and 103 tiles tall (when viewed from above). However, in this example, the robots are in a space which is only 11 tiles wide and 7 tiles tall.
 *
 * The robots are good at navigating over/under each other (due to a combination of springs, extendable legs, and quadcopters), so they can share the same tile and don't interact with each other. Visually, the number of robots on each tile in this example looks like this:
 *
 * 1.12.......
 * ...........
 * ...........
 * ......11.11
 * 1.1........
 * .........1.
 * .......1...
 *
 * These robots have a unique feature for maximum bathroom security: they can teleport. When a robot would run into an edge of the space they're in, they instead teleport to the other side, effectively wrapping around the edges. Here is what robot p=2,4 v=2,-3 does for the first few seconds:
 *
 * Initial state:
 * ...........
 * ...........
 * ...........
 * ...........
 * ..1........
 * ...........
 * ...........
 *
 * After 1 second:
 * ...........
 * ....1......
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 *
 * After 2 seconds:
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * ......1....
 * ...........
 *
 * After 3 seconds:
 * ...........
 * ...........
 * ........1..
 * ...........
 * ...........
 * ...........
 * ...........
 *
 * After 4 seconds:
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * ..........1
 *
 * After 5 seconds:
 * ...........
 * ...........
 * ...........
 * .1.........
 * ...........
 * ...........
 * ...........
 *
 * The Historian can't wait much longer, so you don't have to simulate the robots for very long. Where will the robots be after 100 seconds?
 *
 * In the above example, the number of robots on each tile after 100 seconds has elapsed looks like this:
 *
 * ......2..1.
 * ...........
 * 1..........
 * .11........
 * .....1.....
 * ...12......
 * .1....1....
 *
 * To determine the safest area, count the number of robots in each quadrant after 100 seconds. Robots that are exactly in the middle (horizontally or vertically) don't count as being in any quadrant, so the only relevant robots are:
 *
 * ..... 2..1.
 * ..... .....
 * 1.... .....
 *
 * ..... .....
 * ...12 .....
 * .1... 1....
 *
 * In this example, the quadrants contain 1, 3, 4, and 1 robot. Multiplying these together gives a total safety factor of 12.
 *
 * Predict the motion of the robots in your list within a space which is 101 tiles wide and 103 tiles tall. What will the safety factor be after exactly 100 seconds have elapsed?
 *
 */
private fun part1(lines: List<String>, seconds: Long = 100): Long {
    val robots = lines.parse()

    return robots.safetyFactor(seconds)
}

typealias Robot = Pair<Pair<Long, Long>, Pair<Long, Long>>

private fun List<String>.parse(): List<Robot> = map { line ->
    line.split(' ')
        .take(2)
        .map { robot ->
            robot
                .substringAfter('=')
                .split(',')
                .take(2)
                .map { it.toLong() }
                .let { (a, b) -> a to b }
        }
        .let { (a, b) -> a to b }
}

private fun List<Robot>.width() = asSequence()
    .map { it.first }
    .maxOf { it.first + 1 }

private fun List<Robot>.height() = asSequence()
    .map { it.first }
    .maxOf { it.second + 1 }

private fun List<Robot>.safetyFactor(seconds: Long): Long {
    require(seconds >= 0)

    val w = width()
    val h = height()
    val newPositions = map { (p, v) ->
        val (px, py) = p
        val (vx, vy) = v

        (px + (vx + w) * seconds) % w to (py + (vy + h) * seconds) % h
    }

    val c = IntArray(4)
    newPositions.forEach { (x, y) ->
        when {
            x >= 0 && x < w / 2 -> when {
                y >= 0 && y < h / 2 -> c[0] += 1
                y > h / 2 && y < h -> c[1] += 1
            }

            x > w / 2 && x < w -> when {
                y >= 0 && y < h / 2 -> c[2] += 1
                y > h / 2 && y < h -> c[3] += 1
            }
        }
    }

    return c.fold(1L, Long::times)
}

/**
 * --- Part Two ---
 *
 * During the bathroom break, someone notices that these robots seem awfully similar to ones built and used at the North Pole. If they're the same type of robots, they should have a hard-coded Easter egg: very rarely, most of the robots should arrange themselves into a picture of a Christmas tree.
 *
 * What is the fewest number of seconds that must elapse for the robots to display the Easter egg?
 */
private fun part2(lines: List<String>): Long {
    val robots = lines.parse()
    val xmax = robots.width()
    val ymax = robots.height()

    return (0..<xmax * ymax).minBy { robots.safetyFactor(it) }
}

fun main() {
    val testInput = readInput("aoc_2024/Day14_test")
    val input = readInput("aoc_2024/Day14")

    check(part1(testInput) == 12L)
    part1(input).println()

    part2(input).println()
}
