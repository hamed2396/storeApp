package com.example.storeapp.utils.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object ViewPagerModule {
    @Provides
    fun provideFragmentManager(fragment: Fragment): FragmentManager {
        return fragment.parentFragmentManager
    }

    @Provides

    fun provideLifecycle(fragment: Fragment): Lifecycle {
        return fragment.lifecycle
    }

}