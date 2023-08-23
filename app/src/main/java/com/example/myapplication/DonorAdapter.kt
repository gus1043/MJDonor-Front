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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val currentItem = itemList[position]
                    val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${currentItem.image}"
                    // 이미지 다운로드 및 설정
                    Picasso.get()
                        .load("${imageURL}")
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
                        .into(holder.profile)
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }}
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }


    override fun getItemCount(): Int {
        return if (itemList.size >= 4) 5 else itemList.size
    }
}
