package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRootBinding
import com.example.myapplication.data.DataFactory
import org.locationtech.jts.geom.Coordinate

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityRootBinding.inflate(layoutInflater).also { binding = it }.root)

        binding.btnTransect.setOnClickListener {
            startActivity(Intent(this, TransectActivity::class.java))
        }

        with(binding.sliderProgress) {
            stepSize = 1f
            valueFrom = 0f
            valueTo = 60f
            setValues(0f, 0f)
            addOnChangeListener { slider, _, _ ->
                val (firstValue, secondValue) = slider.values

                binding.farm.furrows = binding.farm.furrows.map {
                    val onSurfaceProgress =
                        (secondValue - slider.valueFrom) / (slider.valueTo - slider.valueFrom)
                    val underSurfaceProgress =
                        (firstValue - slider.valueFrom) / (slider.valueTo - slider.valueFrom)

                    val furrowTotalLength = it.calculateTotalLength()

                    val onSurfaceLength = furrowTotalLength * onSurfaceProgress
                    val underSurfaceLength = furrowTotalLength * underSurfaceProgress

                    val headOfOnSurface = it.calculateCoordinateOf(length = onSurfaceLength)
                    val headOfUnderSurface = it.calculateCoordinateOf(length = underSurfaceLength)

                    it.copy(
                        onSurfaceHead = headOfOnSurface,
                        underSurfaceHead = headOfUnderSurface
                    )
                }
            }
        }

        val azadiTower = DataFactory.readFromAssets(this, "azadi.json")
        binding.farm.fieldCoordinates =
            azadiTower.fieldCoordinates.map { Coordinate(it.lat, it.lng) }
        binding.farm.furrows = azadiTower.furrows.map { coordinateList ->
            FarmView.Furrow(coordinateList.map { Coordinate(it.lat, it.lng) })
        }
        binding.farm.waterOutlet =
            Coordinate(azadiTower.waterOutlet.lat, azadiTower.waterOutlet.lng)
        binding.farm.waterEntrance =
            Coordinate(azadiTower.waterEntrance.lat, azadiTower.waterEntrance.lng)
        binding.farm.slopeXY = azadiTower.slope.x to azadiTower.slope.y
    }
}