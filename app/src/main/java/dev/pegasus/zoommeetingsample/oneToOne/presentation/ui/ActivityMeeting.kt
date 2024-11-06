package dev.pegasus.zoommeetingsample.oneToOne.presentation.ui

import dev.pegasus.zoommeetingsample.R
import us.zoom.sdk.NewMeetingActivity

class ActivityMeeting : NewMeetingActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_meeting
    }

    override fun getLayoutForTablet(): Int {
        return R.layout.activity_meeting_tablet
    }

    override fun isSensorOrientationEnabled(): Boolean {
        return false
    }
}