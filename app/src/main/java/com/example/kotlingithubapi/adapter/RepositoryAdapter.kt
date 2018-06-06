package com.example.kotlingithubapi.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlingithubapi.ChangeSourceListener
import com.example.kotlingithubapi.R
import com.example.kotlingithubapi.api.Repository
import kotlinx.android.synthetic.main.repository_item.view.*


class ReposytoriesAdapter(list: MutableList<Repository>) : RecyclerView.Adapter<ReposytoriesAdapter.ViewHolder>() {

    private val mListeners: MutableList<ChangeSourceListener> = mutableListOf()
    private val mItems: MutableList<Repository> = list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mItems[position]

        holder.textView.text = item.description
        holder.nameView.text = item.name
        holder.langView.text = item.language
        holder.likeView.text = item.watchers
        holder.forksView.text = item.forks
        val dat = item.created_at.date.toString() + "." + (item.created_at.month + 1).toString() + "." + (1900 + item.created_at.year).toString()
        holder.loadView.text = dat


    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view).listen { position, type ->
            changeSource(position)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.text!!
        val nameView = view.name!!
        val langView = view.lang!!
        val likeView = view.like!!
        val forksView = view.forks!!
        val loadView = view.load!!

    }


    fun addListener(listener: ChangeSourceListener) {
        mListeners.add(listener)
    }


    fun changeSource(position: Int) {
        mListeners.forEach {
            it.sourceChanged(position)
        }
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, getItemViewType())
        }
        return this
    }

    operator fun get(position: Int): Repository {
        return mItems[position]
    }

    fun clear() {
        mItems.clear()
    }
}
