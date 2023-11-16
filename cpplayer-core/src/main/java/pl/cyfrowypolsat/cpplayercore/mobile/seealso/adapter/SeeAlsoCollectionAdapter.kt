package pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem

interface SeeAlsoItemClickListener {
    fun onSeeAlsoItemClick(seeAlsoItem: SeeAlsoItem)
    fun onSeeAlsoItemAutoPlay(seeAlsoItem: SeeAlsoItem)
}

class SeeAlsoCollectionAdapter(private val showPosters: Boolean,
                               private val seeAlsoItemClickListener: SeeAlsoItemClickListener,
                               private val autoPlayItemPosition: Int)
    : RecyclerView.Adapter<BaseSeeAlsoItemViewHolder>() {

    var seeAlsoItems: MutableList<SeeAlsoItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return seeAlsoItems.size
    }

    override fun onBindViewHolder(holder: BaseSeeAlsoItemViewHolder,
                                  position: Int) {
        holder.bind(seeAlsoItems[position], position == autoPlayItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BaseSeeAlsoItemViewHolder {
        return if (showPosters) {
            PosterSeeAlsoItemViewHolder(parent, seeAlsoItemClickListener)
        } else {
            ThumbnailSeeAlsoItemViewHolder(parent, seeAlsoItemClickListener)
        }
    }
}
