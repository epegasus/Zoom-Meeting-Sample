package dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network

import android.content.Context
import android.util.Log
import dev.pegasus.zoommeetingsample.oneToOne.presentation.ui.ActivityMeeting
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils.TAG
import us.zoom.sdk.MeetingError
import us.zoom.sdk.MeetingParameter
import us.zoom.sdk.MeetingServiceListener
import us.zoom.sdk.MeetingStatus
import us.zoom.sdk.StartMeetingOptions
import us.zoom.sdk.StartMeetingParamsWithoutLogin
import us.zoom.sdk.ZoomError
import us.zoom.sdk.ZoomSDK
import us.zoom.sdk.ZoomSDKInitParams
import us.zoom.sdk.ZoomSDKInitializeListener
import java.lang.ref.WeakReference

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/1/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class ZoomManager(context: Context?) {

    private val weakReference = WeakReference(context)

    private val zoomSDK get() = ZoomSDK.getInstance()
    private val meetingService get() = zoomSDK.meetingService

    private var initCallback: ((Boolean) -> Unit)? = null

    /**
     * public methods
     *   @see initSdk
     *   @see registerMeetingServiceListener
     *   @see startMeeting
     *
     *   @property initializeListener
     *   @property meetingListener
     *
     */

    fun initSdk(jwtToken: String, callback: (Boolean) -> Unit) {
        this.initCallback = callback

        val context = weakReference.get()
        if (context == null) {
            Log.e(TAG, "initSdk: Context is null")
            callback(false)
            return
        }
        if (jwtToken.isEmpty()) {
            Log.e(TAG, "initSdk: Jwt Token is empty")
            callback(false)
            return
        }

        val initParams = ZoomSDKInitParams().apply {
            this.jwtToken = jwtToken
            this.domain = "zoom.us"
        }
        zoomSDK.initialize(context, initializeListener, initParams)
    }


    private val initializeListener = object : ZoomSDKInitializeListener {
        override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
            initCallback?.invoke(errorCode == ZoomError.ZOOM_ERROR_SUCCESS)
            when (errorCode) {
                ZoomError.ZOOM_ERROR_SUCCESS -> Log.d(TAG, "initializeListener: onZoomSDKInitializeResult: Success")
                ZoomError.ZOOM_ERROR_INVALID_ARGUMENTS -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Invalid arguments provided")
                ZoomError.ZOOM_ERROR_ILLEGAL_APP_KEY_OR_SECRET -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Illegal app key or secret")
                ZoomError.ZOOM_ERROR_NETWORK_UNAVAILABLE -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Network unavailable")
                ZoomError.ZOOM_ERROR_AUTHRET_CLIENT_INCOMPATIBLE -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Client incompatible with Zoom SDK")
                ZoomError.ZOOM_ERROR_AUTHRET_TOKENWRONG -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Authentication token is incorrect")
                ZoomError.ZOOM_ERROR_AUTHRET_KEY_OR_SECRET_ERROR -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: App key or secret is incorrect")
                ZoomError.ZOOM_ERROR_AUTHRET_ACCOUNT_NOT_SUPPORT -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Account type not supported")
                ZoomError.ZOOM_ERROR_AUTHRET_ACCOUNT_NOT_ENABLE_SDK -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: SDK not enabled for account")
                ZoomError.ZOOM_ERROR_AUTHRET_LIMIT_EXCEEDED_EXCEPTION -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Rate limit exceeded")
                ZoomError.ZOOM_ERROR_DEVICE_NOT_SUPPORTED -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Device not supported")
                ZoomError.ZOOM_ERROR_UNKNOWN -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Unknown error occurred")
                ZoomError.ZOOM_ERROR_DOMAIN_DONT_SUPPORT -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Domain not supported")
                else -> Log.e(TAG, "initializeListener: onZoomSDKInitializeResult: Unhandled error code: $errorCode, Internal Error Code: $internalErrorCode")
            }
            if (errorCode == ZoomError.ZOOM_ERROR_SUCCESS) {
                registerMeetingServiceListener()
            }
        }

        override fun onZoomAuthIdentityExpired() {
            Log.e(TAG, "onZoomAuthIdentityExpired: expired")
        }
    }

    private fun registerMeetingServiceListener() {
        meetingService?.addListener(meetingListener)
    }

    private val meetingListener = object : MeetingServiceListener {
        override fun onMeetingStatusChanged(meetingStatus: MeetingStatus?, errorCode: Int, internalErrorCode: Int) {
            when (meetingStatus) {
                MeetingStatus.MEETING_STATUS_IDLE -> Log.d(TAG, "onMeetingStatusChanged: Idle")
                MeetingStatus.MEETING_STATUS_CONNECTING -> Log.d(TAG, "onMeetingStatusChanged: Connecting")
                MeetingStatus.MEETING_STATUS_WAITINGFORHOST -> Log.d(TAG, "onMeetingStatusChanged: Waiting for Host")
                MeetingStatus.MEETING_STATUS_INMEETING -> Log.d(TAG, "onMeetingStatusChanged: In Meeting")
                MeetingStatus.MEETING_STATUS_DISCONNECTING -> Log.d(TAG, "onMeetingStatusChanged: Disconnecting")
                MeetingStatus.MEETING_STATUS_RECONNECTING -> Log.d(TAG, "onMeetingStatusChanged: Reconnecting")
                MeetingStatus.MEETING_STATUS_FAILED -> Log.e(TAG, "onMeetingStatusChanged: Failed")
                MeetingStatus.MEETING_STATUS_ENDED -> Log.d(TAG, "onMeetingStatusChanged: Ended")
                MeetingStatus.MEETING_STATUS_UNLOCKED -> Log.d(TAG, "onMeetingStatusChanged: Unlocked")
                MeetingStatus.MEETING_STATUS_LOCKED -> Log.d(TAG, "onMeetingStatusChanged: Locked")
                MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM -> Log.d(TAG, "onMeetingStatusChanged: In Waiting Room")
                MeetingStatus.MEETING_STATUS_WEBINAR_PROMOTE -> Log.d(TAG, "onMeetingStatusChanged: Webinar Promote")
                MeetingStatus.MEETING_STATUS_WEBINAR_DEPROMOTE -> Log.d(TAG, "onMeetingStatusChanged: Webinar De-Promote")
                MeetingStatus.MEETING_STATUS_JOIN_BREAKOUT_ROOM -> Log.d(TAG, "onMeetingStatusChanged: Join Breakout Room")
                MeetingStatus.MEETING_STATUS_LEAVE_BREAKOUT_ROOM -> Log.d(TAG, "onMeetingStatusChanged: Leave Breakout Room")
                MeetingStatus.MEETING_STATUS_UNKNOWN -> Log.d(TAG, "onMeetingStatusChanged: Unknown")
                else -> Log.d(TAG, "onMeetingStatusChanged: else")
            }
        }

        override fun onMeetingParameterNotification(meetingParameter: MeetingParameter?) {

        }
    }

    fun startMeeting(accessToken: String, callback: (Int) -> Unit) {
        if (!zoomSDK.isInitialized) {
            Log.e(TAG, "startMeeting: ZoomSDK has not been initialized successfully")
            callback(2)
            return
        }
        val context = weakReference.get()
        if (context == null) {
            Log.e(TAG, "startMeeting: Context is null")
            callback(2)
            return
        }

        zoomSDK.zoomUIService.setNewMeetingUI(ActivityMeeting::class.java)

        when (meetingService?.meetingStatus) {
            MeetingStatus.MEETING_STATUS_IDLE -> startNewMeeting(context, accessToken)
            else -> zoomSDK.meetingService?.returnToMeeting(context)
        }
        callback(1)
    }

    private fun startNewMeeting(context: Context, accessToken: String) {
        val startMeetingOptions = StartMeetingOptions().apply {
            no_driving_mode = true
            no_titlebar = false
            no_bottom_toolbar = false
            no_invite = false
        }

        val paramsWithoutLogin = StartMeetingParamsWithoutLogin().apply {
            displayName = ConstantUtils.DISPLAY_NAME
            zoomAccessToken = accessToken
            meetingNo = ""
        }
        val joinMeetingResult = meetingService.startMeetingWithParams(context, paramsWithoutLogin, startMeetingOptions)
        when (joinMeetingResult) {
            MeetingError.MEETING_ERROR_SUCCESS -> Log.i(TAG, "Meeting started successfully.")
            MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE -> Log.e(TAG, "Client version incompatible.")
            MeetingError.MEETING_ERROR_NETWORK_ERROR -> Log.e(TAG, "Network error.")
            MeetingError.MEETING_ERROR_INVALID_ARGUMENTS -> Log.e(TAG, "Invalid arguments for meeting start.")
            MeetingError.MEETING_ERROR_UNKNOWN -> Log.e(TAG, "Unknown error occurred.")
            else -> Log.e(TAG, "Unhandled error code: $joinMeetingResult")
        }
    }
}