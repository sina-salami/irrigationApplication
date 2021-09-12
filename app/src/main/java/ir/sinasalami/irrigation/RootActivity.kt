package ir.sinasalami.irrigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.sinasalami.irrigation.data.DataFactory
import ir.sinasalami.irrigation.databinding.ActivityRootBinding
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

                val onSurfaceProgress =
                    (secondValue - slider.valueFrom) / (slider.valueTo - slider.valueFrom)
                val underSurfaceProgress =
                    (firstValue - slider.valueFrom) / (slider.valueTo - slider.valueFrom)

                binding.farm.onSurfacePercent = (onSurfaceProgress * 100).toInt()
                binding.farm.underSurfacePercent = (underSurfaceProgress * 100).toInt()
            }
        }

        // val mockData = DataFactory.readFromAssets(this, "milad.json")
        // val mockData = DataFactory.readFromAssets(this, "newyork.json")
        val mockData = DataFactory.readFromAssets(this, "rio.json")

        binding.farm.fieldCoordinates =
            mockData.fieldCoordinates.map { Coordinate(it.lat, it.lng) }
        binding.farm.furrows = mockData.furrows.map { coordinateList ->
            FarmView.Furrow(coordinateList.map { Coordinate(it.lat, it.lng) })
        }
        binding.farm.waterOutlet =
            Coordinate(mockData.waterOutlet.lat, mockData.waterOutlet.lng)
        binding.farm.waterEntrance =
            Coordinate(mockData.waterEntrance.lat, mockData.waterEntrance.lng)
        binding.farm.slopeXY = mockData.slope.x to mockData.slope.y
    }
}