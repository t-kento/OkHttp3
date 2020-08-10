package com.example.okhttp3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class OkHttp3Activity : AppCompatActivity() {
    private val customAdapter by lazy { OkHttp3Adapter(this) }
    private var progressDialog: MaterialDialog? = null
    private val handler = Handler()
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        initLayout()
        initData()
    }

    private fun initLayout() {
        initClick()
        initRecyclerView()
        initSwipeRefreshLayout()
    }

    private fun initClick() {
        next.setOnClickListener {
            page++
            updateData2(page)
        }
    }
//    private fun next(i:Int){
//
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://qiita.com/api/v2/items?page=${i}&per_page=5")
//            .build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                println("onFailure call:$call e:$e")
//                handler.post {
//                    swipeRefreshLayout.isRefreshing = false
//                    customAdapter.refresh(listOf())
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                println("onResponse call:$call response:$response")
//                handler.post {
//                    swipeRefreshLayout.isRefreshing = false
//                    response.body?.string()?.also {
//                        val gson = Gson()
//                        val type = object : TypeToken<List<OkHttpItem>>() {}.type
//                        val list = gson.fromJson<List<OkHttpItem>>(it, type)
//                        customAdapter.refresh(list)
//                    } ?: run {
//                        customAdapter.refresh(listOf())
//                    }
//                }
//            }
//        })
//    }

    private fun initRecyclerView() {
        recyclerView.apply {
            adapter = customAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            updateData()
            updateData2(page)
        }
    }

    private fun initData() {
        updateData()
        updateData2(page)
    }

    private fun updateData(page: Int) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://qiita.com/api/v2/items?page=${page}&per_page=20")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("onFailure call:$call e:$e")
                handler.post {
                    swipeRefreshLayout.isRefreshing = false
                    customAdapter.refresh(listOf())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println("onResponse call:$call response:$response")
                handler.post {
                    swipeRefreshLayout.isRefreshing = false
                    response.body?.string()?.also {
                        val gson = Gson()
                        val type = object : TypeToken<List<OkHttpItem>>() {}.type
                        val list = gson.fromJson<List<OkHttpItem>>(it, type)
                        customAdapter.refresh(list)
                    } ?: run {
                        customAdapter.refresh(listOf())
                    }
                }
            }
        })

    }

    private fun updateData2(page: Int) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://qiita.com/api/v2/items?page=${page}&per_page=20")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("onFailure call:$call e:$e")
                handler.post {
                    swipeRefreshLayout.isRefreshing = false
                    customAdapter.refresh(listOf())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println("onResponse call:$call response:$response")
                handler.post {
                    swipeRefreshLayout.isRefreshing = false
                    response.body?.string()?.also {
                        val gson = Gson()
                        val type = object : TypeToken<List<OkHttpItem>>() {}.type
                        val list = gson.fromJson<List<OkHttpItem>>(it, type)
                        customAdapter.refresh(list)
                    } ?: run {
                        customAdapter.refresh(listOf())
                    }
                }
            }
        })

    }

//    private fun addList(): List<String> {
//        val addlist = mutableListOf<String>()
//        val number = i
//        for (i in 0..number) {
//            addlist.add("$i")
//        }
//        return addlist
//    }

    companion object {
        fun start(activity: Activity) =
            activity.startActivity(Intent(activity, OkHttp3Activity::class.java))
    }
}

//private infix fun Callback.companion(any: Any): Callback {
//
//}
