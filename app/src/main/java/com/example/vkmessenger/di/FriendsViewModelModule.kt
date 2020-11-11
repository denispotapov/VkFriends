package com.example.vkmessenger.di

import androidx.lifecycle.ViewModel
import com.example.vkmessenger.ui.friends.FriendsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FriendsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun bindFriendsViewModel(viewModel: FriendsViewModel): ViewModel
}