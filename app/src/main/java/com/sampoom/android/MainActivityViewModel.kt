package com.sampoom.android

import androidx.lifecycle.ViewModel
import com.sampoom.android.core.util.GlobalMessageHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val messageHandler: GlobalMessageHandler
) : ViewModel()