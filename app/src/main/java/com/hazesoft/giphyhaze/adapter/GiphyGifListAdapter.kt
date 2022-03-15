package com.hazesoft.giphyhaze.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hazesoft.giphyhaze.R
import com.hazesoft.giphyhaze.model.GiphyGif

/**
 * Created by Saurav
 * on 3/15/2022
 */
class GiphyGifListAdapter(private val context: Context):
    RecyclerView.Adapter<GiphyGifListAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    private val differCallback = object : DiffUtil.ItemCallback<GiphyGif>() {
        override fun areItemsTheSame(oldItem: GiphyGif, newItem: GiphyGif): Boolean {
            return oldItem.giphyId == newItem.giphyId
        }

        override fun areContentsTheSame(oldItem: GiphyGif, newItem: GiphyGif): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemLayout = itemView.findViewById<ConstraintLayout>(R.id.cl_giphy_gif)
        val gifView = itemView.findViewById<ImageView>(R.id.iv_giphy_gif)
        val favToggle = itemView.findViewById<Button>(R.id.bt_giphy_gif_fav_toggle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_giphy_gif, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val localGiphyGif = differ.currentList.get(position)

        Glide.with(context)
            .asGif()
            .load(localGiphyGif.giphyGifUrl)
            .into(holder.gifView)

        if(localGiphyGif.isFavorite){
            holder.favToggle.text = "Remove from Favorites"
            holder.favToggle.setTextColor(R.color.white)
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
            holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            holder.favToggle.background.setColorFilter(Color.parseColor(holder.itemView.context.resources.getString(R.color.light_pink)), PorterDuff.Mode.SRC_ATOP)

        }else{
            holder.favToggle.text = "Add to Favorites"
            holder.favToggle.setTextColor(R.color.white)
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
            holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            holder.favToggle.background.setColorFilter(Color.parseColor(holder.itemView.context.resources.getString(R.color.dark_grey)), PorterDuff.Mode.SRC_ATOP)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}