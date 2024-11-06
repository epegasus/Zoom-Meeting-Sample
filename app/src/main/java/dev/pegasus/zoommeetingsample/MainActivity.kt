package dev.pegasus.zoommeetingsample

import android.content.Intent
import dev.pegasus.zoommeetingsample.databinding.ActivityMainBinding
import dev.pegasus.zoommeetingsample.oneToOne.presentation.ui.ActivityClient
import dev.pegasus.zoommeetingsample.oneToOne.presentation.ui.ActivityHost
import dev.pegasus.zoommeetingsample.utilities.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreated() {
        binding.mbStartMeetingMain.setOnClickListener { startActivity(Intent(this, ActivityHost::class.java)) }
        binding.mbJoinMeetingMain.setOnClickListener { startActivity(Intent(this, ActivityClient::class.java)) }
    }
}