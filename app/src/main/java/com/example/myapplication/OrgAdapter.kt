import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.R

class OrgAdapter(private val context: Context, private val itemList: List<ItemData>) : RecyclerView.Adapter<OrgAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orgimage: ImageView = itemView.findViewById(R.id.orgimage) ?: throw NullPointerException("selectedImage not found in itemView")
        val orgText: TextView = itemView.findViewById(R.id.organ) ?: throw NullPointerException("orgText not found in itemView")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_org, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.orgimage.setImageResource(currentItem.imageResId)
        holder.orgText.text = currentItem.donLoc
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

