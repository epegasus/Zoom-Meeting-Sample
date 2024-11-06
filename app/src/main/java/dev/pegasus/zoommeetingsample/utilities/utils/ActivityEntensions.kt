package dev.pegasus.zoommeetingsample.utilities.utils

import android.app.Activity
import android.widget.Toast

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}