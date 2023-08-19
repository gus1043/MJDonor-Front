import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.SignupActivity
import com.example.myapplication.databinding.Fragment3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class Fragment3 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPartiAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: Fragment3Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("Account", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sharePreference에서 받아오기
        val id = sharedPreferences.getString("id", "")
        val pw = sharedPreferences.getString("pw", "")

        Log.d("Account", "$id, $pw")

        val userInfoUrl = "http://192.168.0.101:8081/MJDonor/Android/userInfo.jsp?u_id=${id}"
        val url = URL(userInfoUrl)
        val connection = url.openConnection() as HttpURLConnection

        // Sample data for the RecyclerView items
        val itemList = listOf(
            ItemParticipationData("https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png", "아메리칸 스나이퍼", "유니세프 한국위원회", "카카오뱅크 3333-58-481", "23/08/28 19:00", 8555 , true),
            ItemParticipationData("https://www.kasandbox.org/programming-images/avatars/purple-pi-pink.png", "아메리칸 스나이퍼", "유니세프 한국위원회", "카카오뱅크 3333-58-481", "23/08/28 19:00", 8555, false ),
            ItemParticipationData("https://www.kasandbox.org/programming-images/avatars/primosaur-ultimate.png", "아메리칸 스나이퍼", "유니세프 한국위원회", "카카오뱅크 3333-58-481", "23/08/28 19:00", 8555 , true),
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = connection.inputStream
                val content = inputStream.bufferedReader().use { it.readText() }

                // 파싱된 결과에서 필요한 데이터 추출
                val photo = content.substringAfter("Photo: ").substringBefore(", ")
                val name = content.substringAfter("Name: ").substringBefore(", ")
                val userId = content.substringAfter("User ID: ")

                Log.d("please", name)
                Log.d("please", userId)
                Log.d("please", photo)

                withContext(Dispatchers.Main) {
                    binding.name.text = name
                    binding.stdnum.text = userId

                    // 이미지 다운로드 및 설정
                    val extractedBitmap = BitmapFactory.decodeByteArray(photo.toByteArray(), 0, photo.toByteArray().size)
                    binding.profile.setImageBitmap(extractedBitmap)
                }

            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }
        }

        val donationCountUrl = "http://192.168.0.101:8081/MJDonor/Android/donationCountForUser.jsp?u_id=${id}"
        val donationCountUrlConnection = URL(donationCountUrl).openConnection() as HttpURLConnection
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val donationCountInputStream = donationCountUrlConnection.inputStream
                val donationCountContent =
                    donationCountInputStream.bufferedReader().use { it.readText() }

                // 필요한 데이터 추출
                val donationCount =
                    donationCountContent.substringAfter("Donation Count for User $id: ")
                        .substringBefore(", ")

                withContext(Dispatchers.Main) {
                    binding.doncount.text = donationCount
                }
                

            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }

        // totalDonation 데이터 파싱
        val totalDonationUrl = "http://192.168.0.101:8081/MJDonor/Android/sumDonationPointForUser.jsp?u_id=${id}"
        val totalDonationUrlConnection = URL(totalDonationUrl).openConnection() as HttpURLConnection

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val totalDonationInputStream = totalDonationUrlConnection.inputStream
                val totalDonationContent =
                    totalDonationInputStream.bufferedReader().use { it.readText() }

                // 필요한 데이터 추출
                val totalDonation =
                    totalDonationContent.substringAfter("Sum of Donation Points for User $id: ")
                        .substringBefore(", ")

                withContext(Dispatchers.Main) {
                    binding.totaldon.text = totalDonation
                }

            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
                totalDonationUrlConnection.disconnect()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}