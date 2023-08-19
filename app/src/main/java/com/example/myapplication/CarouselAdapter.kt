import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.R

class CarouselAdapter(private val context: Context, private val itemList: List<CarouselItem>) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_donation_main, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position % itemList.size] // Use modulo to create circular wrapping

        // Set data to the views here
        Glide.with(context)
            .load(currentItem.image)
            .into(holder.imageButton)

        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.donLoc
        holder.imageButton.setOnClickListener {
            val intent = Intent(context, DonSelectActivity::class.java)
            intent.putExtra("title", currentItem.title)
            intent.putExtra("donLoc", currentItem.donLoc)
            intent.putExtra("image", currentItem.image)
            intent.putExtra("p_id", currentItem.p_id)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("goal", currentItem.goal)
            intent.putExtra("enddate", currentItem.enddate)
            intent.putExtra("current", currentItem.current)
            context.startActivity(intent)

            if (context is Activity) {
                context.overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
            }
        }


    }

    override fun getItemCount(): Int {
        // 캐러셀 출력 갯수 정하는 거
        return itemList.size
//        return Int.MAX_VALUE
    }
}

// 캐러셀 확장함수 처음에 가운데 보여줌 - max_value일떄 됨
fun RecyclerView.setCarouselLayoutManager() {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    layoutManager.initialPrefetchItemCount = Int.MAX_VALUE // 무한 스크롤을 가능
    this.layoutManager = layoutManager
    this.scrollToPosition(Int.MAX_VALUE / 2)} // RecyclerView를 시작할 때 가운데 항목을 보여주기 위해 스크롤 위치를 Int.MAX_VALUE의 절반
