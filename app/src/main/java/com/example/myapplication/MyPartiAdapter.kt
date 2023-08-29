import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.DonationTree
import com.example.myapplication.R
import com.squareup.picasso.Picasso
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
        val donBtn: Button = itemView.findViewById(R.id.donBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mydon, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${currentItem.imageResId}"
        // 이미지 다운로드 및 설정
        Picasso.get()
            .load("${imageURL}")
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
            .into(holder.participatedimage)

        holder.titleText.text = currentItem.title
        holder.donLocText.text = currentItem.donLoc
        holder.account.text = currentItem.account
        holder.enddate.text = currentItem.enddate
        holder.money.text = currentItem.money.toString()

        holder.participatedimage.setOnClickListener {
            openDonationTree(currentItem)
        }

        val depositMessage = if (currentItem.Deposite == 1) {
            context.getString(R.string.depostie_done)
        } else {
            context.getString(R.string.depostie_not)
        }

        holder.deposit.text = depositMessage

        val textColorResId = if (currentItem.Deposite == 1) {
            R.color.green
        } else {
            R.color.red
        }
        if (currentItem.Deposite == 1) {
            holder.donBtn.visibility = View.VISIBLE
        } else {
            holder.donBtn.visibility = View.INVISIBLE
        }

        holder.deposit.setTextColor(ContextCompat.getColor(context, textColorResId))

        holder.donBtn.setOnClickListener{
            var intent = context.packageManager.getLaunchIntentForPackage("metamask.app")
            if(intent == null) {
                val link = "https://metamask.app.link/dapp/jsp.mjdonor.kro.kr/webapp/Blockchain/blockChainDonate.jsp?virtual_account=${currentItem.account}"
                intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(link)
                }
                context.startActivity(intent)
                return@setOnClickListener
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    private fun openDonationTree(currentItem: ItemParticipationData) {
        val fragment = DonationTree()
        fragment.setImage(currentItem.imageResId)

        val percent = if (currentItem.currentPoint.toDouble() == 0.0) {
            0.0
        } else {
            (currentItem.currentPoint.toDouble() / currentItem.TargetPoint.toDouble()) * 100
        }

        // 데이터를 Bundle에 넣고 프래그먼트에 전달
        val args = Bundle()
        args.putString("title", currentItem.title)
        args.putDouble("percent", percent)
        fragment.arguments = args

        // DonationTree 프래그먼트를 표시
        val transaction = fragmentManager.beginTransaction()
        fragment.show(transaction, "donationTreeDialog")
    }
}
