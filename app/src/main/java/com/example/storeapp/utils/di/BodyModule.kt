package com.example.storeapp.utils.di

import com.example.storeapp.models.address.BodyAddresses
import com.example.storeapp.models.cart.BodyAddToCart
import com.example.storeapp.models.details.BodyAddComment
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.models.profile.BodyUpdateProfile
import com.example.storeapp.models.shipping.BodyCheckCoupon
import com.example.storeapp.models.shipping.BodyUpdateAddress
import com.example.storeapp.ui.wallet.BodyIncreaseWallet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object BodyModule {
    @Provides
    @FragmentScoped
    fun provideBodyLogin() = BodyLogin()

    @Provides
    @FragmentScoped
    fun provideBodyProfile() = BodyUpdateProfile()

    @Provides
    @FragmentScoped
    fun provideBodyIncreaseWallet() = BodyIncreaseWallet()

    @Provides
    @FragmentScoped
    fun provideAddressesBody() = BodyAddresses()

    @Provides
    @FragmentScoped
    fun provideAddToCartBody() = BodyAddToCart()

    @Provides
    @FragmentScoped
    fun provideAddCommentBody() = BodyAddComment()

    @Provides
    @FragmentScoped
    fun bodyCheckCoupon() = BodyCheckCoupon()

    @Provides
    @FragmentScoped
    fun bodyUpdateAddress() = BodyUpdateAddress()
}