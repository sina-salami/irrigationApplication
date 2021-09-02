package com.example.myapplication

import kotlin.random.Random

object DataFactory {
    val azadiTowerCoordinates = listOf(
        FarmView.Coordinate(51.33894988019795, 35.70055926818212),
        FarmView.Coordinate(51.33563029473552, 35.70028605774333),
        FarmView.Coordinate(51.33551568494564, 35.69935181768519),
        FarmView.Coordinate(51.33669304005726, 35.69884354622767),
        FarmView.Coordinate(51.33924422569347, 35.69917284534645),
        FarmView.Coordinate(51.33894988019795, 35.70055926818212)
    )

    val miladTowerCoordinates = listOf(
        FarmView.Coordinate(51.3673715262361, 35.74505358990784),
        FarmView.Coordinate(51.37027891200881, 35.73837003960733),
        FarmView.Coordinate(51.3755134676994, 35.73776272335922),
        FarmView.Coordinate(51.3798550876044, 35.74130467651047),
        FarmView.Coordinate(51.37554714944172, 35.74552452283459)
    )
    val miladEntrance = FarmView.Coordinate(51.365876044, 35.74120467651047)
    val miladOutlet = FarmView.Coordinate(51.375876044, 35.74655967651047)
    val miladFurrow = listOf(
        FarmView.Furrow(
            generateRandomCoordinatesBetween(
                FarmView.Coordinate(51.371276044, 35.73895),
                FarmView.Coordinate(51.370276044, 35.7445),
                8
            )
        ),
        FarmView.Furrow(
            generateRandomCoordinatesBetween(
                FarmView.Coordinate(51.3736, 35.73895),
                FarmView.Coordinate(51.3722, 35.7435),
                6
            )
        ),
        FarmView.Furrow(
            generateRandomCoordinatesBetween(
                FarmView.Coordinate(51.3760, 35.74095),
                FarmView.Coordinate(51.3745, 35.7445),
                4
            )
        )
    )

    val random1Coordinate = listOf(
        FarmView.Coordinate(51.36135321737392, 35.73178870442762),
        FarmView.Coordinate(51.37861909692312, 35.7308034894134),
        FarmView.Coordinate(51.36824002724843, 35.72843100212392),
        FarmView.Coordinate(51.3713200034581, 35.72176766993188),
        FarmView.Coordinate(51.38222792809884, 35.72562962654136),
        FarmView.Coordinate(51.38809342007818, 35.73574893052375),
        FarmView.Coordinate(51.38215813990917, 35.73575425483868),
        FarmView.Coordinate(51.38242383492764, 35.74027235018919),
        FarmView.Coordinate(51.37657770803551, 35.73644196681789),
        FarmView.Coordinate(51.35926337611597, 35.73610501975674),
        FarmView.Coordinate(51.36135321737392, 35.73178870442762),
    )
    val random2Coordinate = listOf(
        FarmView.Coordinate(51.36557942093578, 35.71416220989575),
        FarmView.Coordinate(51.35465785161838, 35.71351100418279),
        FarmView.Coordinate(51.3547300982766, 35.70783630549784),
        FarmView.Coordinate(51.34519894737539, 35.70485174279386),
        FarmView.Coordinate(51.34803072470332, 35.7018682585072),
        FarmView.Coordinate(51.34355028262055, 35.70130607466601),
        FarmView.Coordinate(51.35307422975366, 35.69814245497273),
        FarmView.Coordinate(51.3547239528807, 35.70569492581458),
        FarmView.Coordinate(51.36289869986608, 35.70821345624451),
        FarmView.Coordinate(51.37280356248115, 35.71119161513629),
        FarmView.Coordinate(51.36557942093578, 35.71416220989575)
    )

    val allCoordinates = listOf(
        azadiTowerCoordinates,
        miladTowerCoordinates,
        random1Coordinate,
        random2Coordinate
    )

    fun generateRandomCoordinatesBetween(
        start: FarmView.Coordinate,
        end: FarmView.Coordinate,
        count: Int
    ): List<FarmView.Coordinate> {
        return (0..count)
            .map { Random.nextDouble(minOf(start.x, end.x), maxOf(start.x, end.x)) }
            .reversed()
            .zip(
                (0..count)
                    .map { Random.nextDouble(minOf(start.y, end.y), maxOf(start.y, end.y)) }
                    .sorted()
            )
            .map { FarmView.Coordinate(it.first, it.second) }
    }
}