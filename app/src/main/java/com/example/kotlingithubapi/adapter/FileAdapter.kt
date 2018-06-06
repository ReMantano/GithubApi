package com.example.kotlingithubapi.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlingithubapi.ChangeSourceListener
import com.example.kotlingithubapi.R
import com.example.kotlingithubapi.api.File
import kotlinx.android.synthetic.main.file_item.view.*



class FilesAdapter(list: MutableList<File>) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    private val mListeners: MutableList<ChangeSourceListener> = mutableListOf()
    private val mItems: MutableList<File> = list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.nameView.text = item.name
        if (item.type == "file")holder.typeView.setImageResource(R.drawable.file)
        else holder.typeView.setImageResource(R.drawable.dir)

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.file_item, parent, false)
        return ViewHolder(view).listen { position, type ->
            changeSource(position)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView = view.fileName!!
        val typeView = view.imageType!!


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

    operator fun get(position: Int): File {
        return mItems[position]
    }
}