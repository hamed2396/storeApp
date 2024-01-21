package com.example.storeapp.data.repository.address

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.address.BodyAddresses
import javax.inject.Inject

class AddressesRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getAddresses() = api.getAddresses()
    suspend fun getProvince() = api.getProvince()
    suspend fun getCity(id:Int) = api.getCity(id)
    suspend fun postSubmitAddress(bodyAddresses: BodyAddresses) = api.postAddress(bodyAddresses)
    suspend fun deleteAddress(id:Int) = api.deleteAddress(id)
}