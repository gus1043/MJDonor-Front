import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class DonorAdapter(private val context: Context, private val itemList: List<DonorItem>) : RecyclerView.Adapter<DonorAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: ImageView = itemView.findViewById(R.id.profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_donor, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < 4) {
            val currentItem = itemList[position]
            Glide.with(context)
                .load(currentItem.image)
                .into(holder.profile)

            if (position > 0) {
                val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
                layoutParams.leftMargin = -10.dpToPx(context)
                holder.itemView.layoutParams = layoutParams
            }
        } else {
            holder.profile.setImageResource(R.drawable.baseline_add_24)
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            layoutParams.leftMargin = -10.dpToPx(context)
            holder.itemView.layoutParams = layoutParams
        }

        holder.profile.setOnClickListener {
            val currentItem = itemList[position]
            val uri = currentItem.image
            if (uri.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context.startActivity(intent)
            }
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }


    override fun getItemCount(): Int {
        return if (itemList.size >= 4) 5 else itemList.size
    }
}
