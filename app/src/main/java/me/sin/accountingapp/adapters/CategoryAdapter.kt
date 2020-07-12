package me.sin.accountingapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.sin.accountingapp.R
import me.sin.accountingapp.adapters.viewholder.CategoryViewHolder
import me.sin.accountingapp.bean.CategoryResBean
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.GlobalUtil
import java.util.*

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    private var mCellList: LinkedList<CategoryResBean>
    var currentSelected: String

    private lateinit var onCategoryClickListener: OnCategoryClickListener

    init {
        mCellList = GlobalUtil.instance.costRes
        currentSelected = mCellList[0].title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val res = mCellList[position]
        holder.categoryIv.setImageResource(res.resBlack)
        holder.categoryTv.text = res.title

        holder.itemView.setOnClickListener {
            currentSelected = res.title
            notifyDataSetChanged()
            onCategoryClickListener.onClick(res.title)
        }

        if (holder.categoryTv.text.toString() == currentSelected) {
            holder.categoryBg.setBackgroundResource(R.drawable.bg_add_record_edit_text)
        } else {
            holder.categoryBg.setBackgroundResource(R.color.colorPrimary)
        }

    }

    fun changeType(type: RecordBean.RecordType) {
        mCellList = if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
            GlobalUtil.instance.costRes
        } else {
            GlobalUtil.instance.earnRes
        }

        currentSelected = mCellList[0].title
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mCellList.size

    interface OnCategoryClickListener {
        fun onClick(category: String?)
    }

    fun setOnCategoryClickListener(onCategoryClickListener: OnCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener
    }

}
