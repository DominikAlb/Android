package com.ig.a369254.app_ig

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CreateList (context: Context, home: Markets): BaseAdapter() {

    private val mContext: Context
    val marketMain: Markets
    val sortedList: List<HomeFeed>
    init {
        mContext = context
        marketMain = home
        sortedList = marketMain.markets.sortedWith(compareBy({it.instrumentName}))
    }
    override fun getCount(): Int {
        return marketMain.markets.count()
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItem(position: Int): Any {
        return  "TEST"
    }
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInFlater = LayoutInflater.from(mContext)
        val rowMain = layoutInFlater.inflate(R.layout.row_main, viewGroup, false)
        //display list
        val NameTextView = rowMain.findViewById<TextView>(R.id.textView)

        val tekst = sortedList.get(position)
        NameTextView.text = tekst.instrumentName

        val positionTextView = rowMain.findViewById<TextView>(R.id.position_textview)
        positionTextView.text = tekst.displayOffer.toString()

        return rowMain
    }
}