import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class MyAdapter(private val context: Context, private var itemList: List<ItemData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectedImage: ImageView = itemView.findViewById(R.id.selectedimage1)
        val titleText: TextView = itemView.findViewById(R.id.Title)
        val donLocText: TextView = itemView.findViewById(R.id.donLoc)
        val donateButton: Button = itemView.findViewById(R.id.donBtn)
        val type : TextView= itemView.findViewById(R.id.type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_donation, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${currentItem.imageResId1}"
        Picasso.get()
            .load("${imageURL}")
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
            .into(holder.selectedImage)

        holder.titleText.text = currentItem.title
        holder.donLocText.text = currentItem.donLoc
        holder.type.text = currentItem.type
        holder.donateButton.setOnClickListener {
            val intent = Intent(context, DonSelectActivity::class.java)
            intent.putExtra("title", currentItem.title)
            intent.putExtra("donLoc", currentItem.donLoc)
            intent.putExtra("image1", currentItem.imageResId1)
            intent.putExtra("image2", currentItem.imageResId2)
            intent.putExtra("p_id", currentItem.p_id)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("goal", currentItem.goal)
            intent.putExtra("enddate", currentItem.enddate)
            intent.putExtra("current", currentItem.current)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateData(filteredItemList: List<ItemData>) {
        itemList = filteredItemList
        notifyDataSetChanged()
    }

}
