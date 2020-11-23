package com.example.vkmessenger.di

import androidx.lifecycle.ViewModel
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FriendsOnlineViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FriendsOnlineViewModel::class)
    abstract fun bindFriendsViewModel(viewModel: FriendsOnlineViewModel): ViewModel
}