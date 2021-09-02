package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRootBinding
import kotlin.random.Random

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityRootBinding.inflate(layoutInflater).also { binding = it }.root)

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked.not()) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.btn_milad ->
                    bindSomeData(DataFactory.miladTowerCoordinates, DataFactory.miladFurrow)
                R.id.btn_azadi ->
                    bindSomeData(DataFactory.azadiTowerCoordinates)
                R.id.btn_area1 ->
                    bindSomeData(DataFactory.area1Coordinates)
                R.id.btn_area2 ->
                    bindSomeData(DataFactory.area2Coordinates)
            }
        }

        binding.toggleGroup.check(R.id.btn_milad)
    }

    private fun bindSomeData(
        coordinates: List<FarmView.Coordinate>,
        furrows: List<FarmView.Furrow> = emptyList()
    ) {

        binding.farm.fieldCoordinates = coordinates
        binding.farm.waterEntrance = coordinates.random().run {
            copy(
                x = x - Random.nextDouble(0.002, 0.004),
                y = y - Random.nextDouble(0.002, 0.004)
            )
        }

        binding.farm.waterOutlet = coordinates.random().run {
            copy(
                x = x + Random.nextDouble(0.002, 0.004),
                y = y + Random.nextDouble(0.002, 0.004)
            )
        }
        binding.farm.furrows = furrows

        binding.farm.slopeXY = Random.nextInt(90).toFloat() to Random.nextInt(90).toFloat()
    }
}