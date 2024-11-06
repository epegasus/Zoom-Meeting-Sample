package dev.pegasus.zoommeetingsample.oneToOne.presentation.ui

import android.content.Context
import android.view.View
import androidx.activity.viewModels
import dev.pegasus.zoommeetingsample.databinding.ActivityClientBinding
import dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network.ZoomManager
import dev.pegasus.zoommeetingsample.oneToOne.data.repository.RepositoryHost
import dev.pegasus.zoommeetingsample.oneToOne.domain.useCases.UseCaseHost
import dev.pegasus.zoommeetingsample.oneToOne.presentation.viewModels.ViewModelHost
import dev.pegasus.zoommeetingsample.oneToOne.presentation.viewModels.ViewModelProviderHost
import dev.pegasus.zoommeetingsample.utilities.base.BaseActivity
import dev.pegasus.zoommeetingsample.utilities.utils.showToast
import us.zoom.sdk.JoinMeetingOptions
import us.zoom.sdk.JoinMeetingParams
import us.zoom.sdk.ZoomSDK


class ActivityClient : BaseActivity<ActivityClientBinding>(ActivityClientBinding::inflate) {

    // MVVM
    private val dataSourceZoomManager by lazy { ZoomManager(this) }
    private val repository by lazy { RepositoryHost(dataSourceZoomManager) }
    private val useCase by lazy { UseCaseHost(repository) }
    private val viewModel by viewModels<ViewModelHost> { ViewModelProviderHost(useCase) }

    override fun onCreated() {
        initObservers()

        binding.mbBackClient.setOnClickListener { finish() }
        binding.mbJoinMeetingClient.setOnClickListener { checkValidations() }
    }

    private fun initObservers() {
        viewModel.sdkInitializedLiveData.observe(this) { isInitialized ->
            binding.mbJoinMeetingClient.isEnabled = isInitialized
        }
    }

    private fun checkValidations() {
        val number = binding.etNumberClient.text.toString()
        val password = binding.etPasswordClient.text.toString()
        if (number.isEmpty() || password.isEmpty()) {
            showToast("Please enter all fields")
            return
        }
        joinMeeting(number, password)
    }

    private fun joinMeeting(number: String, password: String) {
        binding.progressBarClient.visibility = View.VISIBLE
        binding.mbJoinMeetingClient.isEnabled = false

        joinMeeting(context = this, meetingNumber = number, password = password)
    }

    private fun joinMeeting(context: Context, meetingNumber: String, password: String) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            this.displayName = "Client Side (Yaqoob)"
            this.meetingNo = meetingNumber
            this.password = password
        }
        meetingService.joinMeetingWithParams(context, params, options)
    }
}