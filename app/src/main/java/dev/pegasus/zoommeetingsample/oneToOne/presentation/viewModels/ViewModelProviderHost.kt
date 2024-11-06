package dev.pegasus.zoommeetingsample.oneToOne.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.pegasus.zoommeetingsample.oneToOne.domain.useCases.UseCaseHost

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class ViewModelProviderHost(private val useCaseHost: UseCaseHost) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelHost::class.java)) {
            return ViewModelHost(useCaseHost) as T
        }
        return super.create(modelClass)
    }
}