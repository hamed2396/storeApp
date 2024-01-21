package com.example.storeapp.data.network

import com.example.storeapp.models.SimpleResponse
import com.example.storeapp.models.address.BodyAddresses
import com.example.storeapp.models.address.ResponseAddresses
import com.example.storeapp.models.address.ResponseProvince
import com.example.storeapp.models.address.ResponseSubmitAddress
import com.example.storeapp.models.cart.BodyAddToCart
import com.example.storeapp.models.cart.ResponseCart
import com.example.storeapp.models.cart.ResponseUpdateCart
import com.example.storeapp.models.categories.ResponseCategories
import com.example.storeapp.models.comment.ResponseComments
import com.example.storeapp.models.comment.ResponseDelete
import com.example.storeapp.models.details.BodyAddComment
import com.example.storeapp.models.details.ResponseChart
import com.example.storeapp.models.details.ResponseDetail
import com.example.storeapp.models.details.ResponseDetailFavorite
import com.example.storeapp.models.details.ResponseFeatures
import com.example.storeapp.models.details.ResponseProductComments
import com.example.storeapp.models.favorite.ResponseFavorite
import com.example.storeapp.models.home.ResponseBanners
import com.example.storeapp.models.home.ResponseDiscount
import com.example.storeapp.models.home.ResponseProducts
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.models.login.ResponseLogin
import com.example.storeapp.models.login.ResponseVerify
import com.example.storeapp.models.profile.BodyUpdateProfile
import com.example.storeapp.models.profile.ResponseOrders
import com.example.storeapp.models.profile.ResponseProfile
import com.example.storeapp.models.profile.ResponseWallet
import com.example.storeapp.models.search.ResponseSearch
import com.example.storeapp.models.shipping.BodyCheckCoupon
import com.example.storeapp.models.shipping.BodyUpdateAddress
import com.example.storeapp.models.shipping.ResponseCheckCoupon
import com.example.storeapp.models.shipping.ResponseShipment
import com.example.storeapp.models.shipping.ResponseUpdateAddress
import com.example.storeapp.models.wallet.ResponseIncreaseWallet
import com.example.storeapp.ui.wallet.BodyIncreaseWallet
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiServices {
    @POST("auth/login")
    suspend fun postLogin(@Body bodyLogin: BodyLogin): Response<ResponseLogin>

    @POST("auth/login/verify")
    suspend fun postVerify(@Body bodyLogin: BodyLogin): Response<ResponseVerify>

    @GET("auth/detail")
    suspend fun getProfileData(): Response<ResponseProfile>

    @GET("ad/swiper/{slug}")
    suspend fun getBannersList(@Path("slug") slug: String): Response<ResponseBanners>

    @GET("offers/discount/{slug}")
    suspend fun getDiscountList(@Path("slug") slug: String): Response<ResponseDiscount>

    @GET("category/pro/{slug}")
    suspend fun getProductList(
        @Path("slug") slug: String, @QueryMap queries: Map<String, String>
    ): Response<ResponseProducts>

    @GET("search")
    suspend fun searchProduct(@QueryMap queries: Map<String, String>): Response<ResponseSearch>

    @GET("menu")
    suspend fun getCategoriesData(): Response<ResponseCategories>

    @GET("auth/wallet")
    suspend fun getWalletData(): Response<ResponseWallet>

    @POST("auth/avatar")
    suspend fun postAvatar(@Body postBody: RequestBody): Response<Unit>

    @PUT("auth/update")
    suspend fun putProfileUpdate(@Body body: BodyUpdateProfile): Response<ResponseProfile>

    @POST("auth/wallet")
    suspend fun postIncreaseWallet(@Body body: BodyIncreaseWallet): Response<ResponseIncreaseWallet>

    @GET("auth/comments")
    suspend fun getComments(): Response<ResponseComments>

    @DELETE("auth/comment/{id}")
    suspend fun deleteComment(@Path("id") id: Int): Response<ResponseDelete>

    @GET("auth/favorites")
    suspend fun getFavorite(): Response<ResponseFavorite>

    @DELETE("auth/favorite/{id}")
    suspend fun deleteFavorite(@Path("id") id: Int): Response<ResponseDelete>

    @GET("auth/addresses")
    suspend fun getAddresses(): Response<ResponseAddresses>

    @GET("auth/address/provinces")
    suspend fun getProvince(): Response<ResponseProvince>

    @GET("auth/address/provinces")
    suspend fun getCity(@Query("provinceId") id: Int): Response<ResponseProvince>

    @POST("auth/address")
    suspend fun postAddress(@Body bodyAddresses: BodyAddresses): Response<ResponseSubmitAddress>

    @DELETE("auth/address/remove/{id}")
    suspend fun deleteAddress(@Path("id") id: Int): Response<ResponseDelete>

    @GET("auth/orders")
    suspend fun getOrderStatus(@Query("status") status: String): Response<ResponseOrders>

    @GET("product/{id}")
    suspend fun getDetail(@Path("id") id: Int): Response<ResponseDetail>

    @POST("product/{id}/like")
    suspend fun postFavorite(@Path("id") id: Int): Response<ResponseDetailFavorite>

    @POST("product/{id}/add_to_cart")
    suspend fun postAddToCart(
        @Path("id") id: Int,
        @Body body: BodyAddToCart
    ): Response<SimpleResponse>

    @GET("product/{id}/features")
    suspend fun getFeatures(@Path("id") id: Int): Response<ResponseFeatures>

    @POST("product/{id}/comments")
    suspend fun postAddComment(
        @Path("id") id: Int,
        @Body body: BodyAddComment
    ): Response<SimpleResponse>

    @GET("product/{id}/comments")
    suspend fun getProductComments(@Path("id") id: Int): Response<ResponseProductComments>

    @GET("product/{id}/price-chart")
    suspend fun getProductChart(@Path("id") id: Int): Response<ResponseChart>

    @GET("cart")
    suspend fun getCartList(): Response<ResponseCart>

    @PUT("cart/{id}/increment")
    suspend fun putCartIncrement(@Path("id") id: Int): Response<ResponseUpdateCart>

    @PUT("cart/{id}/decrement")
    suspend fun putCartDecrement(@Path("id") id: Int): Response<ResponseUpdateCart>

    @DELETE("cart/{id}")
    suspend fun deleteCartProduct(@Path("id") id: Int): Response<ResponseUpdateCart>

    @GET("cart/continue")
    suspend fun getCartContinue(): Response<ResponseUpdateCart>

    @GET("shipping")
    suspend fun getShipment(): Response<ResponseShipment>

    @POST("check/coupon")
    suspend fun postCheckCoupon(@Body body: BodyCheckCoupon): Response<ResponseCheckCoupon>

    @PUT("shipping/set/address")
    suspend fun putUpdateAddress(@Body body: BodyUpdateAddress): Response<ResponseUpdateAddress>

    @POST("payment")
    suspend fun postPayment(@Body body: BodyCheckCoupon): Response<ResponseIncreaseWallet>


}