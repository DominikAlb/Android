package com.ig.a369254.app_ig

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.AsyncTask.execute
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import android.view.View
import android.widget.*
import android.support.v7.widget.*
import android.support.v7.widget.RecyclerView.LayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    //It makes for me spinner
    var languages = arrayOf("UK", "France", "Germany")
    var spinner:Spinner? = null
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

        var con: Context = this
        var url: String = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/en_GB/igi"
        if(position===0) {
            url = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/en_GB/igi"
        } else if(position===1) {
            url = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/fr_FR/frm"
        } else if(position===2) {
            url = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/de_DE/dem"
        } else {
            //default
            url = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/en_GB/igi"
        }
        //Start connecting with site
        fetchJson(url, con)
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //spinner - HERE
        spinner = this.spinner_simple
        spinner!!.setOnItemSelectedListener(this)

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)

        

    }
    //list - HERE
    private class MyCustomAdapter(context: Context, home: Markets): BaseAdapter() {

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
    fun fetchJson(str: String, con: Context) {
        //println("Connecting")

        //list, which I want to display
        val listView = findViewById<ListView>(R.id.main_listview)
        //

        //making connection with site
        var url = str
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println("UWaga \n" + body)

                val gson = GsonBuilder().create()

                var home: Markets = gson.fromJson(body, Markets::class.java)
                runOnUiThread{
                    listView.adapter = MyCustomAdapter(con, home)
                }

            }
            override fun onFailure(call: Call?, e: IOException?) {
                println("Nie działa")
            }
        })

    }
}
//classes, which I need for GSON
class HomeFeed(val instrumentName: String, val instrumentVersion: Int,
               val displayPeriod : String, val epic : String,
               val exchangeId : String, val displayBid : Double,
               val displayOffer : Double, val updateTime : Int,
               val netChange : Double, val scaled : Boolean,
               val timezoneOffset : Int) {

}
class Markets(val markets: List<HomeFeed>, chartFormat: String, val lightstreamerEndpoint : String,
              val configuration : String) {}