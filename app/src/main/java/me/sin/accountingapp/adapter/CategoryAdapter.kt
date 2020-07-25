package me.sin.accountingapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.sin.accountingapp.R
import me.sin.accountingapp.adapter.viewholder.CategoryViewHolder
import me.sin.accountingapp.bean.CategoryResBean
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.util.ResUtil
import java.util.*

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    private var mCellList: LinkedList<CategoryResBean>
    var currentSelected: String

    private lateinit var onCategoryClickListener: OnCategoryClickListener

    init {
        mCellList = ResUtil.costRes
        currentSelected = mCellList[0].title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        with(mCellList[position]) {
            holder.categoryIv.setImageResource(resBlack)
            holder.categoryTv.text = title

            holder.itemView.setOnClickListener {
                currentSelected = title
                notifyDataSetChanged()
                onCategoryClickListener.onClick(title)
            }
        }
        if (holder.categoryTv.text.toString() == currentSelected) {
            holder.categoryBg.setBackgroundResource(R.drawable.bg_add_record_edit_text)
        } else {
            holder.categoryBg.setBackgroundResource(R.color.colorPrimary)
        }
    }

    fun changeType(type: Int) {
        mCellList = if (type == RecordBean.TYPE_EXPENSE) {
            ResUtil.costRes
        } else {
            ResUtil.earnRes
        }

        currentSelected = mCellList[0].title
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mCellList.size

    interface OnCategoryClickListener {
        fun onClick(category: String)
    }

    fun setOnCategoryClickListener(onCategoryClickListener: OnCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener
    }

}

