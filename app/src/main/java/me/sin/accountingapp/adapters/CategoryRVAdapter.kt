package me.sin.accountingapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.sin.accountingapp.R
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.GlobalUtil
import me.sin.accountingapp.viewholder.CategoryViewHolder

class CategoryRVAdapter(mContext: Context) : RecyclerView.Adapter<CategoryViewHolder>() {

    private val mInflater: LayoutInflater

    private var cellList = GlobalUtil.instance.costRes

    var selected: String
        private set

    private lateinit var onCategoryClickListener: OnCategoryClickListener

    fun setOnCategoryClickListener(onCategoryClickListener: OnCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener
    }

    init {
        GlobalUtil.instance.context = mContext
        mInflater = LayoutInflater.from(mContext)
        selected = cellList[0].title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = mInflater.inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val res = cellList[position]
        holder.imageView.setImageResource(res.resBlack)
        holder.textView.text = res.title

        holder.itemView.setOnClickListener {
            selected = res.title
            notifyDataSetChanged()
            onCategoryClickListener.onClick(res.title)
        }

        if (holder.textView.text.toString() == selected) {
            holder.background.setBackgroundResource(R.drawable.bg_add_record_edit_text)
        } else {
            holder.background.setBackgroundResource(R.color.colorPrimary)
        }

    }

    fun changeType(type: RecordBean.RecordType) {
        cellList = if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
            GlobalUtil.instance.costRes
        } else {
            GlobalUtil.instance.earnRes
        }

        selected = cellList[0].title
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = cellList.size

    interface OnCategoryClickListener {
        fun onClick(category: String?)
    }

}

