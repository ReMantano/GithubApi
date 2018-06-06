package com.example.kotlingithubapi.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kotlingithubapi.ChangeSourceListener
import com.example.kotlingithubapi.R
import com.example.kotlingithubapi.api.User
import kotlinx.android.synthetic.main.user_item.view.*



class UserAdapter(list: MutableList<User>,context:Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val mListeners: MutableList<ChangeSourceListener> = mutableListOf()
    private val mItems: MutableList<User> = list
    private val  context: Context = context

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.loginView.text = item.login
        holder.typeView.text = item.type
        Glide.with(context)
                .load(item.avatar_url)
                .into(holder.imgView)


    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.user_item, parent, false)
        return ViewHolder(view).listen { position, type ->
            changeSource(position)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loginView = view.login!!
        val typeView = view.type!!
        val imgView = view.imgProfile!!




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

    operator fun get(position: Int): User {
        return mItems[position]
    }

    fun clear(){
        mItems.clear()
    }
}
