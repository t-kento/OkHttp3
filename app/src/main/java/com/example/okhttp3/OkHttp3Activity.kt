package com.example.okhttp3

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private val addList: MutableList<OkHttpItem> = mutableListOf()
    var page = 1
    private var isLoading = false

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
//            addList()
//            page++
            updateData(true)
            println("押されました")
        }
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            adapter = customAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy == 0) {
                        return
                    }
                    val totalItemCount = customAdapter.itemCount
                    val lastVisibleItem =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    println("$totalItemCount")
                    if(!isLoading && lastVisibleItem>=totalItemCount-10)
                        updateData(true)
                }
            })
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            updateData()
        }
    }

    private fun initData() {
        updateData()
    }

    private fun updateData(isAdd: Boolean = false) {
        if (isLoading){
            return
        }else{
            isLoading=true
        }
        if (isAdd) {
                page++
            } else {
                page = 1
            }
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://qiita.com/api/v2/items?page=${page}&per_page=20")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("onFailure call:$call e:$e")
                handler.post {
                    swipeRefreshLayout.isRefreshing = false
                    if (isAdd) {
                        customAdapter.addList(listOf())
                    } else {
                        customAdapter.refresh(listOf())
                    }
                    isLoading=false
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
                        if (isAdd) {
                            customAdapter.addList(list)
                        } else {
                            customAdapter.refresh(list)
                        }
                    } ?: run {
                        if (isAdd) {
                            customAdapter.addList(listOf())
                        } else {
                            customAdapter.refresh(listOf())
                        }
                    }
                    isLoading=false
                }
            }
        })
    }
}
