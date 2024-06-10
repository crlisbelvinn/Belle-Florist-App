package com.example.myappbelle.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myappbelle.adapter.SectionsPagerAdapter
import com.example.myappbelle.databinding.ActivityDetailBinding
import com.example.myappbelle.remote.response.DetailUserResponse
import com.example.myappbelle.remote.response.ItemsItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getSerializableExtra("ITEM_DETAIL") as? ItemsItem

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        if (item != null) {
            sectionsPagerAdapter.username = item.login.orEmpty()
        }

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> ""
            }
        }.attach()

        if (item != null) {
            if (userDetailViewModel.detail.value == null) {
                item.login?.let { userDetailViewModel.findDetail(it) }
            }
            userDetailViewModel.detail.observe(this) { detail ->
                if (detail != null) {
                    bindUser(detail)
                }
            }
            userDetailViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            userDetailViewModel.errorMessage.observe(this) { message ->
                Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
            }
        } else {
            val message = "Transfer error"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindUser(detail: DetailUserResponse) {
        Glide.with(this)
            .load(detail.avatarUrl)
            .into(binding.ivAvatar)
        binding.tvName.text = detail.name
        binding.tvUsername.text = detail.login
        binding.tvFollower.text = "${detail.followers.toString()} Followers"
        binding.tvFollowing.text = "${detail.following.toString()} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
