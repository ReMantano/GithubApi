package com.example.kotlingithubapi.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.example.kotlingithubapi.ChangeSourceListener
import com.example.kotlingithubapi.CheckInternetConection
import com.example.kotlingithubapi.R
import com.example.kotlingithubapi.adapter.FilesAdapter
import com.example.kotlingithubapi.api.File
import com.example.kotlingithubapi.api.SearchRepository
import com.example.kotlingithubapi.api.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


const val INTENT_NAME_NAME = "name"
const val INTENT_PATH = "path"

class FileActivity :  AppCompatActivity(), ChangeSourceListener {

    @BindView(R.id.list)
    lateinit var listView: RecyclerView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter: FilesAdapter

    private val list: MutableList<File> = mutableListOf()
    private lateinit var name_repository: String
    private lateinit var login: String
    private lateinit var cm: ConnectivityManager

    override fun sourceChanged(position: Int) {

        if (adapter[position].type == "dir") {
            val intent = Intent(applicationContext, FileActivity::class.java)
            intent.putExtra(INTENT_PATH, adapter[position].path)
            intent.putExtra(INTENT_LOGIN,login)
            intent.putExtra(INTENT_NAME_NAME, name_repository)

            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm
        cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val bar : ProgressBar = findViewById<ProgressBar>(R.id.progressBar_2)
        bar.visibility = ProgressBar.VISIBLE


        val path = intent.getStringExtra(INTENT_PATH)
        name_repository = intent.getStringExtra(INTENT_NAME_NAME)
        login = intent.getStringExtra(INTENT_LOGIN)

        setTitle(name_repository + "/"+path)

        while(!CheckInternetConection.conect(cm)) TimeUnit.SECONDS.sleep(1)

        compositeDisposable.add(
                repository.searchFile( login,name_repository,path)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->


                            for(view: File in result){
                                if (view.type == "dir") list.add(0,view)
                                else list.add(view)
                            }

                            adapter = FilesAdapter(list)
                            adapter.addListener(this)
                            listView.adapter = adapter

                            bar.visibility = ProgressBar.INVISIBLE
                        })
        )


    }

}
