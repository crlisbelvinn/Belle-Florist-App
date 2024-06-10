package com.example.myappbelle.ui

import com.example.myappbelle.adapter.UserAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappbelle.databinding.FragmentFollowsBinding
import com.example.myappbelle.remote.response.ItemsItem
import com.example.myappbelle.remote.retrofit.ApiConfig

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding
    private val followViewModel by viewModels<FollowViewModel>()

    private var position: Int = 0
    private var username: String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }

        if (position == 1) {
            followViewModel.fetchData(ApiConfig.getApiService().getAccountFollowers(username))
            followViewModel.dataFetched.observe(viewLifecycleOwner) { dataFetched ->
                showRecyclerView(dataFetched)
            }
        } else {
            followViewModel.fetchData(ApiConfig.getApiService().getAccountFollowing(username))
            followViewModel.dataFetched.observe(viewLifecycleOwner) { dataFetched ->
                showRecyclerView(dataFetched)
            }
        }
        followViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        followViewModel.errorMessage.observe(viewLifecycleOwner){ message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecyclerView(items: List<ItemsItem>?) {
        binding.rvFollows.layoutManager = LinearLayoutManager(requireContext())

        val adapter = UserAdapter()
        binding.rvFollows.adapter = adapter

        items?.let {
            adapter.submitList(it)
        }
    }
    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
