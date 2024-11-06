package dev.pegasus.zoommeetingsample.oneToOne.presentation.ui

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import dev.pegasus.zoommeetingsample.R
import dev.pegasus.zoommeetingsample.databinding.ActivityHostBinding
import dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network.ZoomManager
import dev.pegasus.zoommeetingsample.oneToOne.data.repository.RepositoryHost
import dev.pegasus.zoommeetingsample.oneToOne.domain.useCases.UseCaseHost
import dev.pegasus.zoommeetingsample.oneToOne.presentation.viewModels.ViewModelHost
import dev.pegasus.zoommeetingsample.oneToOne.presentation.viewModels.ViewModelProviderHost
import dev.pegasus.zoommeetingsample.utilities.base.BaseActivity
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils.TAG

class ActivityHost : BaseActivity<ActivityHostBinding>(ActivityHostBinding::inflate) {

    // MVVM
    private val dataSourceZoomManager by lazy { ZoomManager(this) }
    private val repository by lazy { RepositoryHost(dataSourceZoomManager) }
    private val useCase by lazy { UseCaseHost(repository) }
    private val viewModel by viewModels<ViewModelHost> { ViewModelProviderHost(useCase) }

    override fun onCreated() {
        initObservers()

        binding.mbStartMeetingHost.setOnClickListener { viewModel.startMeeting() }
    }

    private fun initObservers() {
        viewModel.sdkInitializedLiveData.observe(this) { isInitialized ->
            binding.mbStartMeetingHost.isEnabled = isInitialized
            when (isInitialized) {
                true -> binding.mtvStatusHost.text = getString(R.string.initialization_success)
                false -> binding.mtvStatusHost.text = getString(R.string.initialization_failure)
            }
        }

        viewModel.intentLiveData.observe(this) { intent ->
            startActivity(intent)
        }

        viewModel.meetingStatusLiveData.observe(this) { status ->
            binding.progressBarHost.isVisible = status == 0
            when (status) {
                0 -> binding.mtvStatusHost.text = getString(R.string.meeting_status_progress)
                1 -> binding.mtvStatusHost.text = getString(R.string.meeting_status_success)
                2 -> binding.mtvStatusHost.text = getString(R.string.meeting_status_failure)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d(TAG, "onNewIntent: called: $intent")
        viewModel.onNewIntent(intent)

        /*if (ConstantUtils.ACTION_RETURN_FROM_MEETING == intent.action) {
            startMeeting()
        }*/
    }

    private fun startMeeting() {
    }
}