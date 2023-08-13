import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.DonationTree
import com.example.myapplication.R

class MyPartiAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val itemList: List<ItemParticipationData>) : RecyclerView.Adapter<MyPartiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val participatedimage: ImageButton = itemView.findViewById(R.id.participatedimage)
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
        holder.participatedimage.setImageResource(currentItem.imageResId)
        holder.titleText.text = currentItem.title
        holder.donLocText.text = currentItem.donLoc
        holder.account.text = currentItem.account
        holder.enddate.text = currentItem.enddate
        holder.money.text = currentItem.money.toString()
        holder.participatedimage.setOnClickListener {
            openDonationTree()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    private fun openDonationTree() {
        val fragment = DonationTree()
        fragment.show(fragmentManager, "donationTreeDialog")
    }
}
