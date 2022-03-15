package com.hazesoft.giphyhaze.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hazesoft.giphyhaze.R
import com.hazesoft.giphyhaze.model.GiphyGif


/**
 * Created by Saurav
 * on 3/15/2022
 */
class GiphyGifListAdapter(private val context: Context, private val listener: OnFavoriteToggleClicked, private val type: String):
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
        // slightly diff layout for diff rv
        return if(type == "linear"){
            val itemView = layoutInflater.inflate(R.layout.item_giphy_gif, parent, false)
            ViewHolder(itemView)
        }else{
            val itemView = layoutInflater.inflate(R.layout.item_giphy_gif_grid, parent, false)
            ViewHolder(itemView)
        }
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val localGiphyGif = differ.currentList.get(position)

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 6f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(64))
        requestOptions = requestOptions.placeholder(circularProgressDrawable)


        Glide.with(context)
            .asGif()
            .centerCrop()
            .apply(requestOptions)
            .load(localGiphyGif.giphyGifUrl)
            .into(holder.gifView)

        if(localGiphyGif.isFavorite){

            if(type == "linear"){
                holder.favToggle.text = "Remove from Favorites"
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
                holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }else{
                holder.favToggle.text = "Remove"
                holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            holder.favToggle.background.setColorFilter(Color.parseColor(holder.itemView.context.resources.getString(R.color.dark_grey)), PorterDuff.Mode.SRC_ATOP)

        }else{
            holder.favToggle.text = "Add to Favorites"
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
            holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            holder.favToggle.background.setColorFilter(Color.parseColor(holder.itemView.context.resources.getString(R.color.light_pink)), PorterDuff.Mode.SRC_ATOP)
        }

        holder.favToggle.setOnClickListener {
            listener.onFavClicked(localGiphyGif)
            localGiphyGif.isFavorite = !localGiphyGif.isFavorite

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnFavoriteToggleClicked{
        fun onFavClicked(giphyGif: GiphyGif)
    }

}