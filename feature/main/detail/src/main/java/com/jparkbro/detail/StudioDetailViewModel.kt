package com.jparkbro.detail

import androidx.lifecycle.ViewModel
import com.jparkbro.detail.navigation.StudioDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = StudioDetailViewModel.Factory::class)
class StudioDetailViewModel @AssistedInject constructor(
    @Assisted val initData: StudioDetail
) : ViewModel() {



    @AssistedFactory
    interface Factory {
        fun create(
            initData: StudioDetail
        ): StudioDetailViewModel
    }
}

//sealed interface