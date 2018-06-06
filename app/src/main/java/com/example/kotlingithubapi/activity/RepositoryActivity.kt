package com.example.kotlingithubapi.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import android.content.Context
import android.net.ConnectivityManager
import android.widget.*
import com.bumptech.glide.Glide
import com.example.kotlingithubapi.ChangeSourceListener
import com.example.kotlingithubapi.CheckInternetConection.conect
import com.example.kotlingithubapi.R
import com.example.kotlingithubapi.adapter.ReposytoriesAdapter
import com.example.kotlingithubapi.api.Repository
import com.example.kotlingithubapi.api.SearchRepository
import com.example.kotlingithubapi.api.SearchRepositoryProvider


const val tag: String = "MainActivity"
const val INTENT_LOGIN = "login"

class RepositoryActivity : AppCompatActivity(), ChangeSourceListener,SearchView.OnQueryTextListener {

    @BindView(R.id.list)
    lateinit var listView: RecyclerView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter: ReposytoriesAdapter
    lateinit var search:SearchView
    lateinit var bar:ProgressBar

    private val list: MutableList<Repository> = mutableListOf()
    private lateinit var login: String
    private lateinit var cm: ConnectivityManager




    override fun onQueryTextSubmit(p0: String?): Boolean {

        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        compositeDisposable.clear()
        adapter.clear()
        listView.adapter.notifyDataSetChanged()

        while(!conect(cm)) TimeUnit.SECONDS.sleep(1)
        if (p0 != null)
            search(p0.toString())
        else query()

        return false
    }

    override fun sourceChanged(position: Int) {

        val intent = Intent(applicationContext, FileActivity::class.java)
        intent.putExtra(INTENT_NAME_NAME, adapter[position].name)
        intent.putExtra(INTENT_LOGIN,login)
        intent.putExtra(INTENT_PATH, "")
        startActivity(intent)
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm
        search =   findViewById<SearchView>(R.id.searchText)
        search.setOnQueryTextListener(this)
        cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        login = intent.getStringExtra(INTENT_LOGIN)

        while(!conect(cm)) TimeUnit.SECONDS.sleep(1)
        profile()
        query()

    }

    fun query() {

        bar = findViewById<ProgressBar>(R.id.progressBar_1)
        search = findViewById<SearchView>(R.id.searchText)

        bar.visibility =  ProgressBar.VISIBLE

        compositeDisposable.add(
                repository.searchRepository(login)

                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            list.addAll(result)

                            adapter = ReposytoriesAdapter(list)
                            adapter.addListener(this)
                            listView.adapter = adapter

                            bar.visibility = ProgressBar.INVISIBLE
                        })
        )

        search.setOnQueryTextListener(this)

    }

    fun profile(){
        compositeDisposable.add(
                repository.searchUser(login)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->

                            findViewById<TextView>(R.id.profile_name).text = result.name
                            findViewById<TextView>(R.id.profile_login).text = result.login
                            findViewById<TextView>(R.id.profile_location).text = result.location
                            findViewById<TextView>(R.id.profile_folowers).text = "followers "+ result.followers
                            findViewById<TextView>(R.id.profile_repos).text = "searchFile " + result.public_repos
                            Glide.with(this)
                                    .load(result.avatar_url)
                                    .into(findViewById<ImageView>(R.id.avatar))

                        }



                        ))
    }

    fun search(s: String){
        bar = findViewById<ProgressBar>(R.id.progressBar_1)
        search = findViewById<SearchView>(R.id.searchText)

        bar.visibility =  ProgressBar.VISIBLE

        compositeDisposable.add(
                repository.searchReposytories(s)
                        .debounce(500,TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->


                            for (source: Repository in result)
                                for( i: Repository in source.items)
                                    list.add(i)


                            adapter = ReposytoriesAdapter(list)
                            adapter.addListener(this)
                            listView.adapter = adapter

                            bar.visibility = ProgressBar.INVISIBLE
                        })
        )

        search.setOnQueryTextListener(this)

    }



}


