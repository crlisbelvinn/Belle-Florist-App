package com.example.myappbelle.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappbelle.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _dataFetched = MutableLiveData<List<ItemsItem>?>()
    val dataFetched: LiveData<List<ItemsItem>?> = _dataFetched

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun fetchData(client: Call<List<ItemsItem>>) {
        _isLoading.value = true
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val dataList = response.body()
                    _dataFetched.value = dataList
                } else {
                    errorMessage.value = "List is empty"
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                errorMessage.value = "Failed to fetch data: ${t.message}"
            }
        })
    }
}