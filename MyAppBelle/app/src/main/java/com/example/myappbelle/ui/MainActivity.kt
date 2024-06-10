package com.example.myappbelle.ui

import com.example.myappbelle.adapter.UserAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappbelle.vm.MainViewModel
import com.example.myappbelle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        userAdapter = UserAdapter()
        binding.rvUser.adapter = userAdapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                binding.searchBar.setText(binding.searchView.text)
                searchView.hide()
                val querySearch = searchView.text.toString()
                mainViewModel.searchUsers(querySearch)
                false
            }
        }

        showRecycleView()

        mainViewModel.userList.observe(this@MainActivity) { user ->
            userAdapter.submitList(user)
        }

        mainViewModel.isLoading.observe(this@MainActivity) {
            showLoading(it)
        }
        mainViewModel.errorMessage.observe(this@MainActivity) { message ->
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecycleView() {
        userAdapter = UserAdapter()
        binding.rvUser.adapter = userAdapter
        binding.rvUser.layoutManager = LinearLayoutManager(this)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
