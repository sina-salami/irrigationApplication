package ir.sinasalami.irrigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.sinasalami.irrigation.databinding.ActivityTransectBinding

class TransectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityTransectBinding.inflate(layoutInflater).also { binding = it }.root)

        with(binding.sliderLow) {
            stepSize = 1f
            valueFrom = 0f
            valueTo = 120f
            addOnChangeListener { slider, _, _ ->
                binding.transect.lowWidth = slider.value
            }
        }
        with(binding.sliderMid) {
            stepSize = 1f
            valueFrom = 0f
            valueTo = 120f
            addOnChangeListener { slider, _, _ ->
                binding.transect.middleWidth = slider.value
            }
        }
        with(binding.sliderHigh) {
            stepSize = 1f
            valueFrom = 0f
            valueTo = 120f
            addOnChangeListener { slider, _, _ ->
                binding.transect.highWidth = slider.value
            }
        }
        with(binding.sliderHeight) {
            stepSize = 1f
            valueFrom = 0f
            valueTo = 100f
            addOnChangeListener { slider, _, _ ->
                binding.transect.bedHeight = slider.value
            }
        }
        with(binding.sliderWaterHeight) {
            stepSize = 1f
            valueFrom = 0f
            valueTo = 100f
            addOnChangeListener { slider, _, _ ->
                binding.transect.waterHeight = slider.value
            }
        }

        binding.sliderLow.value = 30f
        binding.sliderMid.value = 45f
        binding.sliderHigh.value = 90f
        binding.sliderHeight.value = 80f
        binding.sliderWaterHeight.value = 50f
    }
}