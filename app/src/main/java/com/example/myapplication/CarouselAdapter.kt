import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DonSelectActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CarouselAdapter(private val context: Context, private val itemList: List<CarouselItem>) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {
    private lateinit var sharedPreferences: SharedPreferences
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_donation_main, parent, false)
        sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position % itemList.size] // Use modulo to create circular wrapping

        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${currentItem.image1}"
                    // 이미지 다운로드 및 설정
                    Picasso.get()
                        .load("${imageURL}")
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
                        .into(holder.imageButton)
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }
        }

        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.donLoc


        val sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE) // 앱 자체에 데이터 저장
        val isFirstTime = sharedPreferences.getBoolean("login", false)
        holder.imageButton.setOnClickListener {
            if (!isFirstTime) {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                if (context is Activity) {
                    context.overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
                }
            }else {
                val intent = Intent(context, DonSelectActivity::class.java)
                intent.putExtra("title", currentItem.title)
                intent.putExtra("donLoc", currentItem.donLoc)
                intent.putExtra("image1", currentItem.image1)
                intent.putExtra("image2", currentItem.image2)
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
