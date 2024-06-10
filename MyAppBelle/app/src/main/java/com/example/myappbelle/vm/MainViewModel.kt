package com.example.myappbelle.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappbelle.remote.response.GithubResponse
import com.example.myappbelle.remote.response.ItemsItem
import com.example.myappbelle.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {
   private val _userList = MutableLiveData<List<ItemsItem>?>()
    val userList: LiveData<List<ItemsItem>?> = _userList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    val errorMessage: MutableLiveData<String> = MutableLiveData()


    init {
        findUsers("q")
    }

    private fun findUsers(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAccount(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userList.value = responseBody.items
                    }
                } else {
                    errorMessage.value = "User not found"
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                errorMessage.value = "Failed to fetch data: ${t.message}"
            }
        })
    }

    fun searchUsers(query: String) {
        if (query.isNotBlank()) {
            findUsers(query)
        }
    }

}