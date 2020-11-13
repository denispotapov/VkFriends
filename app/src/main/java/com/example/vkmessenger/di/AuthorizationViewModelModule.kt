package com.example.vkmessenger.di

import androidx.lifecycle.ViewModel
import com.example.vkmessenger.ui.authorization.AuthorizationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthorizationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    abstract fun bindAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel
}