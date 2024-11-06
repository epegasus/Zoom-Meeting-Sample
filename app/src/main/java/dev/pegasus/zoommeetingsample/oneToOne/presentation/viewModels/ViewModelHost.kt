package dev.pegasus.zoommeetingsample.oneToOne.presentation.viewModels

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pegasus.zoommeetingsample.oneToOne.domain.useCases.UseCaseHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class ViewModelHost(private val useCaseHost: UseCaseHost) : ViewModel() {

    private val _sdkInitializedLiveData = MutableLiveData<Boolean>()
    val sdkInitializedLiveData: LiveData<Boolean> get() = _sdkInitializedLiveData

    /**
     * @property _meetingStatusLiveData
     *      0: Progress
     *      1: Success
     *      2: Failure
     */

    private val _meetingStatusLiveData = MutableLiveData<Int>()
    val meetingStatusLiveData: LiveData<Int> get() = _meetingStatusLiveData

    private val _intentLiveData = MutableLiveData<Intent>()
    val intentLiveData: LiveData<Intent> get() = _intentLiveData

    init {
        initializeSdk()
    }

    private fun initializeSdk() {
        val jwtToken = useCaseHost.generateJwtToken()
        useCaseHost.initSdk(jwtToken) { isInitialized ->
            _sdkInitializedLiveData.postValue(isInitialized)
        }
    }

    fun startMeeting() {
        _intentLiveData.value = useCaseHost.getAuthorizationCodeIntent()
    }

    fun onNewIntent(intent: Intent) = viewModelScope.launch(Dispatchers.IO) {
        _meetingStatusLiveData.postValue(0)
        useCaseHost.onNewIntent(intent) {
            _meetingStatusLiveData.postValue(1)
        }
    }
}