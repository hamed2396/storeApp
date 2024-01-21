package com.example.storeapp.ui.viewpager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.storeapp.ui.viewpager.DetailChartFragment
import com.example.storeapp.ui.viewpager.comments.DetailCommentFragment
import com.example.storeapp.ui.viewpager.DetailFeaturesFragment
import javax.inject.Inject

class PagerAdapter @Inject constructor(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DetailCommentFragment()
            }

            1 -> {
                DetailFeaturesFragment()
            }

            else -> {
                DetailChartFragment()
            }
        }
    }

}