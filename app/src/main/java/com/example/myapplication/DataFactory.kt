package com.example.myapplication

import org.locationtech.jts.geom.Coordinate
import kotlin.random.Random

object DataFactory {
    val azadiTowerCoordinates = listOf(
        Coordinate(51.33894988019795, 35.70055926818212),
        Coordinate(51.33563029473552, 35.70028605774333),
        Coordinate(51.33551568494564, 35.69935181768519),
        Coordinate(51.33669304005726, 35.69884354622767),
        Coordinate(51.33924422569347, 35.69917284534645),
        Coordinate(51.33894988019795, 35.70055926818212)
    )

    val miladTowerCoordinates = listOf(
        Coordinate(51.3673715262361, 35.74505358990784),
        Coordinate(51.37027891200881, 35.73837003960733),
        Coordinate(51.3755134676994, 35.73776272335922),
        Coordinate(51.3798550876044, 35.74130467651047),
        Coordinate(51.37554714944172, 35.74552452283459)
    )
    val miladEntrance = Coordinate(51.365876044, 35.74120467651047)
    val miladOutlet = Coordinate(51.375876044, 35.74655967651047)
    val miladFurrow = listOf(
        FarmView.Furrow(
            generateRandomCoordinatesBetween(
                Coordinate(51.371276044, 35.73895),
                Coordinate(51.370276044, 35.7445),
                8
            )
        ),
        FarmView.Furrow(
            generateRandomCoordinatesBetween(
                Coordinate(51.3736, 35.73895),
                Coordinate(51.3722, 35.7435),
                6
            )
        ),
        FarmView.Furrow(
            generateRandomCoordinatesBetween(
                Coordinate(51.3760, 35.74095),
                Coordinate(51.3745, 35.7445),
                4
            )
        )
    )

    val area1Coordinates = listOf(
        Coordinate(0.0, 0.0),
        Coordinate(100.0, 0.0),
        Coordinate(100.0, 100.0),
        Coordinate(0.0, 100.0),
    )
    val area1Furrow = listOf(
        FarmView.Furrow(
            listOf(
                Coordinate(10.0, 10.0),
                Coordinate(20.0, 10.0),
                Coordinate(20.0, 20.0),
                Coordinate(30.0, 20.0),
                Coordinate(30.0, 30.0),
                Coordinate(40.0, 30.0),
                Coordinate(40.0, 40.0),
            ).reversed()
        )
    )
    val area2Coordinates = listOf(
        Coordinate(51.36557942093578, 35.71416220989575),
        Coordinate(51.35465785161838, 35.71351100418279),
        Coordinate(51.3547300982766, 35.70783630549784),
        Coordinate(51.34519894737539, 35.70485174279386),
        Coordinate(51.34803072470332, 35.7018682585072),
        Coordinate(51.34355028262055, 35.70130607466601),
        Coordinate(51.35307422975366, 35.69814245497273),
        Coordinate(51.3547239528807, 35.70569492581458),
        Coordinate(51.36289869986608, 35.70821345624451),
        Coordinate(51.37280356248115, 35.71119161513629),
        Coordinate(51.36557942093578, 35.71416220989575)
    )

    fun generateRandomCoordinatesBetween(
        start: Coordinate,
        end: Coordinate,
        count: Int
    ): List<Coordinate> {
        return (0..count)
            .map { Random.nextDouble(minOf(start.x, end.x), maxOf(start.x, end.x)) }
            .reversed()
            .zip(
                (0..count)
                    .map { Random.nextDouble(minOf(start.y, end.y), maxOf(start.y, end.y)) }
                    .sorted()
            )
            .map { Coordinate(it.first, it.second) }
    }
}