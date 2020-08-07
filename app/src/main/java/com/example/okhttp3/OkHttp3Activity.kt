package com.example.okhttp3

import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class OkHttp3Activity : AppCompatActivity(){
    private val customAdapter by lazy { OkHttp3Adapter(this) }
    private var progressDialog: MaterialDialog? = null
    private val handler = Handler()

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
            next()
        }
    }

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
        }
    }

    private fun initData() {
        updateData()
    }

    private fun updateData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://qiita.com/api/v2/items?page=1&per_page=20")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    swipeRefreshLayout.isRefreshing = false
                    customAdapter.refresh(listOf())
                }
            }

            override fun onResponse(call: Call, response: Response) {
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
    }



    companion object {
        fun start(activity: Activity) = activity.startActivity(Intent(activity, OkHttp3Activity::class.java))
    }
}
}

private infix fun Callback.companion(any: Any): Callback {

}
