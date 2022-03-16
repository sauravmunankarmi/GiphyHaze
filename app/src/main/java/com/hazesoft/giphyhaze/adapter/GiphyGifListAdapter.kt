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
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hazesoft.giphyhaze.R
import com.hazesoft.giphyhaze.model.GiphyGif


/**
 * Created by Saurav
 * on 3/15/2022
 */
class GiphyGifListAdapter(private val context: Context, private val listener: OnFavoriteToggleClicked):
    PagingDataAdapter<GiphyGif, GiphyGifListAdapter.ViewHolder>(differCallback) {

    private val layoutInflater = LayoutInflater.from(context)

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<GiphyGif>() {
            override fun areItemsTheSame(oldItem: GiphyGif, newItem: GiphyGif): Boolean {
                return oldItem.giphyId == newItem.giphyId
            }

            override fun areContentsTheSame(oldItem: GiphyGif, newItem: GiphyGif): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gifView: ImageView = itemView.findViewById<ImageView>(R.id.iv_giphy_gif)
        val favToggle: Button = itemView.findViewById<Button>(R.id.bt_giphy_gif_fav_toggle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_giphy_gif, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val localGiphyGif = getItem(position)

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
            .load(localGiphyGif?.giphyGifUrl)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(holder.gifView)

        if(localGiphyGif?.isFavorite == true){
            holder.favToggle.text = "Remove from Favorites"
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
            holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            holder.favToggle.background.setColorFilter(Color.parseColor(holder.itemView.context.resources.getString(R.color.dark_grey)), PorterDuff.Mode.SRC_ATOP)

        }else{
            holder.favToggle.text = "Add to Favorites"
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
            holder.favToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            holder.favToggle.background.setColorFilter(Color.parseColor(holder.itemView.context.resources.getString(R.color.light_pink)), PorterDuff.Mode.SRC_ATOP)
        }

        holder.favToggle.setOnClickListener {
            listener.onFavClicked(localGiphyGif!!)
        }

    }

    interface OnFavoriteToggleClicked{
        fun onFavClicked(giphyGif: GiphyGif)
    }

}