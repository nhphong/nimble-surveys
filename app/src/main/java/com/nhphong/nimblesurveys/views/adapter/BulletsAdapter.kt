package com.nhphong.nimblesurveys.views.adapter

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.nhphong.nimblesurveys.R
import kotlinx.android.synthetic.main.bullet.view.*

class BulletsAdapter : RecyclerView.Adapter<BulletsAdapter.BulletHolder>() {

  private var data: List<Boolean> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BulletHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.bullet, parent, false)
    return BulletHolder(view)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun onBindViewHolder(holder: BulletHolder, position: Int) {
    holder.view.bullet.isSelected = data[position]
  }

  fun syncWithViewPager(viewPager: ViewPager, bulletsView: RecyclerView) {
    viewPager.adapter?.run {
      registerDataSetObserver(object : DataSetObserver() {
        override fun onChanged() {
          super.onChanged()
          setBullets(count, viewPager.currentItem, bulletsView)
        }
      })

      viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
          setBullets(count, viewPager.currentItem, bulletsView, true)
        }
      })
    }
  }

  //TODO Optimize this method by using notifyItemChanged(), instead of notifyDataSetChanged()
  private fun setBullets(
    numOfBullets: Int,
    selectedBullet: Int,
    bulletsView: RecyclerView,
    smoothScroll: Boolean = false
  ) {
    data = (0 until numOfBullets).map {
      it == selectedBullet
    }
    if (smoothScroll) {
      bulletsView.smoothScrollToPosition(selectedBullet)
    } else {
      bulletsView.scrollToPosition(selectedBullet)
    }
  }

  inner class BulletHolder(val view: View) : RecyclerView.ViewHolder(view)
}
