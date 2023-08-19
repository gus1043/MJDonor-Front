import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.DonationTree
import com.example.myapplication.R
import org.w3c.dom.Text

class MyPartiAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val itemList: List<ItemParticipationData>) : RecyclerView.Adapter<MyPartiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val participatedimage: ImageButton = itemView.findViewById(R.id.participatedimage)
        val titleText: TextView = itemView.findViewById(R.id.Title)
        val donLocText: TextView = itemView.findViewById(R.id.donLoc)
        val account: TextView = itemView.findViewById(R.id.account)
        val enddate: TextView = itemView.findViewById(R.id.enddate)
        val money: TextView = itemView.findViewById(R.id.money)
        val deposit: TextView = itemView.findViewById(R.id.deposit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mydon, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        Glide.with(context)
            .load(currentItem.imageResId)
            .into(holder.participatedimage)

        holder.titleText.text = currentItem.title
        holder.donLocText.text = currentItem.donLoc
        holder.account.text = currentItem.account
        holder.enddate.text = currentItem.enddate
        holder.money.text = currentItem.money.toString()

        holder.participatedimage.setOnClickListener {
            openDonationTree(currentItem.imageResId) // 이미지 URL 설정
        }

        val depositMessage = if (currentItem.Deposite == true) {
            context.getString(R.string.depostie_done)
        } else {
            context.getString(R.string.depostie_not)
        }

        holder.deposit.text = depositMessage

        val textColorResId = if (currentItem.Deposite == true) {
            R.color.green
        } else {
            R.color.red
        }

        holder.deposit.setTextColor(ContextCompat.getColor(context, textColorResId))

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    private fun openDonationTree(imageUrl: String) {
        val fragment = DonationTree()
        fragment.setImage(imageUrl) // 이미지 URL 설정

        // DonationTree 프래그먼트를 표시
        val transaction = fragmentManager.beginTransaction()
        fragment.show(transaction, "donationTreeDialog")
    }
}
