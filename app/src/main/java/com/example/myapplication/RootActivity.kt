package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityRootBinding.inflate(layoutInflater).also { binding = it }.root)

        binding.farm.fieldCoordinates = DataFactory.miladTowerCoordinates
        binding.farm.waterEntrance = DataFactory.miladEntrance
        binding.farm.waterOutlet = DataFactory.miladOutlet
    }
}