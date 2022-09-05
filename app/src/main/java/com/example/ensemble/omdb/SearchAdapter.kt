package com.example.ensemble.omdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ensemble.R
import com.example.ensemble.service.OmdbData
import com.squareup.picasso.Picasso

class SearchAdapter(private var mList: List<OmdbData>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.omdb_search_item, parent, false)

        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateList(list: List<OmdbData>) {
        mList = list
        notifyDataSetChanged()
    }

    class SearchViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val mPoster: ImageView = ItemView.findViewById(R.id.poster_img)
        private val mTitle: TextView = ItemView.findViewById(R.id.title_txt)
        private val mYear: TextView = ItemView.findViewById(R.id.year_txt)

        fun bind(item: OmdbData) {
            Picasso.get().load(item.poster).resize(300, 0).centerInside().into(mPoster)
            mTitle.text = item.title
            mYear.text = item.year
        }
    }
}

