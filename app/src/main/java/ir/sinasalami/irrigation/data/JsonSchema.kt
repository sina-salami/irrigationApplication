package ir.sinasalami.irrigation.data


data class JRoot(
    val fieldCoordinates: List<JCoordinate>,
    val furrows: List<List<JCoordinate>>,
    val waterEntrance: JCoordinate,
    val waterOutlet: JCoordinate,
    val slope: JSlope
) {
    data class JCoordinate(val lat: Double, val lng: Double)
    data class JSlope(val x: Float, val y: Float)
}