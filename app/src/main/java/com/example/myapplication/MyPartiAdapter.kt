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
import com.example.myapplication.DonationSelectionActivity
import com.example.myapplication.R

class MyPartiAdapter(private val context: Context, private val itemList: List<ItemParticipationData>) : RecyclerView.Adapter<MyPartiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectedImage: ImageView = itemView.findViewById(R.id.selectedimage1)
        val titleText: TextView = itemView.findViewById(R.id.Title)
        val donLocText: TextView = itemView.findViewById(R.id.donLoc)
        val account: TextView = itemView.findViewById(R.id.account)
        val enddate: TextView = itemView.findViewById(R.id.enddate)
        val money: TextView = itemView.findViewById(R.id.money)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mydon, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Set data to the views here
        holder.selectedImage.setImageResource(currentItem.imageResId)
        holder.titleText.text = currentItem.title
        holder.donLocText.text = currentItem.donLoc
        holder.account.text = currentItem.account
        holder.enddate.text = currentItem.enddate
        holder.money.text = currentItem.money.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
