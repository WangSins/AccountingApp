package me.sin.accountingapp.utils

import me.sin.accountingapp.R
import me.sin.accountingapp.base.BaseApplication
import me.sin.accountingapp.bean.CategoryResBean
import me.sin.accountingapp.database.RecordDatabaseHelper
import java.util.*

object GlobalUtil {

    private val costIconRes = intArrayOf(
            R.drawable.icon_general_white, R.drawable.icon_food_white, R.drawable.icon_drinking_white,
            R.drawable.icon_groceries_white, R.drawable.icon_shopping_white, R.drawable.icon_personal_white,
            R.drawable.icon_entertain_white, R.drawable.icon_movie_white, R.drawable.icon_social_white,
            R.drawable.icon_transport_white, R.drawable.icon_appstore_white, R.drawable.icon_mobile_white,
            R.drawable.icon_computer_white, R.drawable.icon_gift_white, R.drawable.icon_house_white,
            R.drawable.icon_travel_white, R.drawable.icon_ticket_white, R.drawable.icon_book_white,
            R.drawable.icon_medical_white, R.drawable.icon_transfer_white)
    private val costIconResBlack = intArrayOf(
            R.drawable.icon_general, R.drawable.icon_food, R.drawable.icon_drinking,
            R.drawable.icon_groceries, R.drawable.icon_shopping, R.drawable.icon_presonal,
            R.drawable.icon_entertain, R.drawable.icon_movie, R.drawable.icon_social,
            R.drawable.icon_transport, R.drawable.icon_appstore, R.drawable.icon_mobile,
            R.drawable.icon_computer, R.drawable.icon_gift, R.drawable.icon_house,
            R.drawable.icon_travel, R.drawable.icon_ticket, R.drawable.icon_book,
            R.drawable.icon_medical, R.drawable.icon_transfer)
    private val costTitle = arrayOf(
            "全部", "食品", "饮料", "杂货",
            "购物", "私人", "娱乐", "电影",
            "社交", "交通", "应用商店", "手机",
            "电脑", "礼物", "住房", "旅游",
            "票", "书籍", "医疗", "搬迁")
    private val earnIconRes = intArrayOf(
            R.drawable.icon_general_white, R.drawable.icon_reimburse_white, R.drawable.icon_salary_white,
            R.drawable.icon_redpocket_white, R.drawable.icon_parttime_white, R.drawable.icon_bonus_white,
            R.drawable.icon_investment_white)
    private val earnIconResBlack = intArrayOf(
            R.drawable.icon_general, R.drawable.icon_reimburse, R.drawable.icon_salary,
            R.drawable.icon_redpocket, R.drawable.icon_parttime, R.drawable.icon_bonus,
            R.drawable.icon_investment)
    private val earnTitle = arrayOf("全部", "报销", "工资", "红包", "兼职", "奖金", "投资")
    var databaseHelper: RecordDatabaseHelper = RecordDatabaseHelper(BaseApplication.context, RecordDatabaseHelper.DB_NAME, null, 1)
    var costRes = LinkedList<CategoryResBean>()
    var earnRes = LinkedList<CategoryResBean>()


    fun getResourceIcon(category: String): Int {
        for (res in costRes) {
            if (res.title == category) {
                return res.resWhite
            }
        }

        for (res in earnRes) {
            if (res.title == category) {
                return res.resWhite
            }
        }
        return costRes[0].resWhite
    }

    init {
        for (i in costTitle.indices) {
            if (costRes.size < costTitle.size) {
                costRes.add(CategoryResBean().apply {
                    title = costTitle[i]
                    resBlack = costIconResBlack[i]
                    resWhite = costIconRes[i]
                })
            }
        }
        for (i in earnTitle.indices) {
            if (earnRes.size < earnTitle.size) {
                earnRes.add(CategoryResBean().apply {
                    title = earnTitle[i]
                    resBlack = earnIconResBlack[i]
                    resWhite = earnIconRes[i]
                })
            }
        }
    }
}