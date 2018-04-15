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
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
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
            url = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/en_GB/igi"
        }
        fetchJson(url, con)
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinner = this.spinner_simple
        spinner!!.setOnItemSelectedListener(this)

        val spinerCreate = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinerCreate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(spinerCreate)

    }
    fun fetchJson(str: String, con: Context) {

        val listView = findViewById<ListView>(R.id.main_listview)

        var url = str
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()

                val gson = GsonBuilder().create()

                var home: Markets = gson.fromJson(body, Markets::class.java)
                runOnUiThread{
                    listView.adapter = CreateList(con, home)
                }

            }
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("System.err", "unsuccessful connection")
            }
        })

    }
}

