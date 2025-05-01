package jpark.bro.data.common

import android.app.Activity
import jpark.bro.domain.common.ActivityProvider
import javax.inject.Inject

class ActivityProviderImpl @Inject constructor(
    private val activity: Activity
) : ActivityProvider {
    override fun getActivity(): Any {
        return activity
    }
}