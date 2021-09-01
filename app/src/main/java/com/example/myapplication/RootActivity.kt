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

        binding.btnRandom.setOnClickListener {
            bindSomeData()
        }

        binding.farm.fieldCoordinates = DataFactory.miladTowerCoordinates
        binding.farm.waterEntrance = DataFactory.miladEntrance
        binding.farm.waterOutlet = DataFactory.miladOutlet
        binding.farm.slopeXY = 20f to 14.4f

    }

    private fun bindSomeData() {
        val coordinates = DataFactory.allCoordinates.random()

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

        binding.farm.slopeXY = Random.nextFloat() to Random.nextFloat()
    }
}