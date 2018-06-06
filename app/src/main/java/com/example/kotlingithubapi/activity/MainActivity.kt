package com.example.kotlingithubapi.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.SearchView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.kotlingithubapi.ChangeSourceListener
import com.example.kotlingithubapi.CheckInternetConection.conect
import com.example.kotlingithubapi.R
import com.example.kotlingithubapi.adapter.UserAdapter
import com.example.kotlingithubapi.api.SearchRepository
import com.example.kotlingithubapi.api.SearchRepositoryProvider
import com.example.kotlingithubapi.api.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit



class MainActivity :AppCompatActivity(), ChangeSourceListener, SearchView.OnQueryTextListener {

    @BindView(R.id.listUser)
    lateinit var listView: RecyclerView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter: UserAdapter
    lateinit var search:SearchView
    lateinit var bar:ProgressBar

    private val list: MutableList<User> = mutableListOf()
    private lateinit var cm : ConnectivityManager

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        compositeDisposable.clear()
        adapter.clear()
        listView.adapter.notifyDataSetChanged()

        while(!conect(cm)) TimeUnit.SECONDS.sleep(1)
        if (p0 != null) query(p0.toString())
        else listUser()
        return false
    }



    override fun sourceChanged(position: Int) {

        val intent = Intent(applicationContext, RepositoryActivity::class.java)
        intent.putExtra(INTENT_LOGIN, adapter[position].login)
        startActivity(intent)
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm
        cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        while(!conect(cm)) TimeUnit.SECONDS.sleep(1)
        listUser()

    }

    fun listUser() {

        bar = findViewById<ProgressBar>(R.id.progressBar_3)
        search = findViewById<SearchView>(R.id.searchText_2)

        bar.visibility =  ProgressBar.VISIBLE

        compositeDisposable.add(
                repository.searchListUser()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            list.addAll(result)

                            adapter = UserAdapter(list, this)
                            adapter.addListener(this)
                            listView.adapter = adapter

                            bar.visibility = ProgressBar.INVISIBLE
                        })
        )

        search.setOnQueryTextListener(this)

    }
    fun query(s:String = "") {

        bar = findViewById<ProgressBar>(R.id.progressBar_3)
        search = findViewById<SearchView>(R.id.searchText_2)

        bar.visibility =  ProgressBar.VISIBLE

        compositeDisposable.add(
                repository.searchPeoples(s)
                        .debounce(500,TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->


                            for (u: User in result){
                                for (i:User in u.items)
                                    list.add(i)
                            }

                            adapter = UserAdapter(list, this)
                            adapter.addListener(this)
                            listView.adapter = adapter

                            bar.visibility = ProgressBar.INVISIBLE

                        })
        )

        search.setOnQueryTextListener(this)

    }
}
