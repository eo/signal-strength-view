package eo.view.signalstrength.sample

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import eo.view.signalstrength.SignalStrength
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupThemeSwitcher()
        setupSignalLevelControl()
    }

    private fun setupThemeSwitcher() {
        if (signalStrengthView.theme == SignalStrength.Theme.ROUNDED) {
            roundedThemeChip.isChecked = true
        } else {
            sharpThemeChip.isChecked = true
        }

        themeChipGroup.setOnCheckedChangeListener { _, chipId ->
            when (chipId) {
                R.id.roundedThemeChip -> signalStrengthView.theme = SignalStrength.Theme.ROUNDED
                R.id.sharpThemeChip -> signalStrengthView.theme = SignalStrength.Theme.SHARP
                else -> {
                    if (signalStrengthView.theme == SignalStrength.Theme.ROUNDED) {
                        roundedThemeChip.isChecked = true
                    } else {
                        sharpThemeChip.isChecked = true
                    }
                }
            }
        }
    }

    private fun setupSignalLevelControl() {
        signalLevelSeekBar.progress = signalStrengthView.signalLevel
        signalLevelValueText.text =
                getString(R.string.signal_level_value, signalStrengthView.signalLevel)

        signalLevelSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                signalStrengthView.signalLevel = progress
                signalLevelValueText.text = getString(R.string.signal_level_value, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // no-op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // no-op
            }
        })
    }
}
