package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRootBinding
import org.locationtech.jts.geom.Coordinate
import kotlin.random.Random

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityRootBinding.inflate(layoutInflater).also { binding = it }.root)

        binding.btnTransect.setOnClickListener {
            startActivity(Intent(this, TransectActivity::class.java))
        }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked.not()) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.btn_milad ->
                    bindSomeData(
                        DataFactory.miladTowerCoordinates,
                        DataFactory.miladFurrow.map { it.copy(onSurfaceHead = it.coordinates.random()) })
                R.id.btn_azadi ->
                    bindSomeData(DataFactory.azadiTowerCoordinates)
                R.id.btn_area1 ->
                    bindSomeData(DataFactory.area1Coordinates, DataFactory.area1Furrow)
                R.id.btn_area2 ->
                    bindSomeData(DataFactory.area2Coordinates)
            }
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

        binding.toggleGroup.check(R.id.btn_milad)
    }

    private fun bindSomeData(
        coordinates: List<Coordinate>,
        furrows: List<FarmView.Furrow> = emptyList()
    ) {

        binding.farm.fieldCoordinates = coordinates
        binding.farm.waterEntrance = coordinates.random().copy().apply {
            x -= Random.nextDouble(0.002, 0.004)
            y -= Random.nextDouble(0.002, 0.004)
        }
        binding.farm.waterOutlet = coordinates.random().copy().apply {
            x -= Random.nextDouble(0.002, 0.004)
            y -= Random.nextDouble(0.002, 0.004)
        }
        binding.farm.furrows = furrows

        binding.farm.slopeXY = Random.nextInt(90).toFloat() to Random.nextInt(90).toFloat()
    }
}