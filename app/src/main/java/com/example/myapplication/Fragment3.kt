import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.SignupActivity
import com.example.myapplication.databinding.Fragment3Binding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

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

        val userInfoUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/userInfo.jsp?u_id=${id}"
        val url = URL(userInfoUrl)
        val connection = url.openConnection() as HttpURLConnection


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = connection.inputStream
                val content = inputStream.bufferedReader().use { it.readText() }

                // 파싱된 결과에서 필요한 데이터 추출
                val photo = content.substringAfter("Photo: ").substringBefore(", ")
                val name = content.substringAfter("Name: ").substringBefore(", ")
                val userId = content.substringAfter("User ID: ")
                val userProfileUrl = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${photo}"

                Log.d("please", name)
                Log.d("please", userId)
                Log.d("please", photo)
                Log.d("please", userProfileUrl)

                withContext(Dispatchers.Main) {
                    binding.name.text = name
                    binding.stdnum.text = userId

                    // 이미지 다운로드 및 설정
                    Picasso.get()
                        .load("${userProfileUrl}")
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
                        .into(binding.profile)
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }
        }

        val donationCountUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/donationCountForUser.jsp?u_id=${id}"
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
        val totalDonationUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/sumDonationPointForUser.jsp?u_id=${id}"
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


        val MylistUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/contributedDonationList.jsp?u_id=${id}"
        Log.d("please D_url", MylistUrl)
        val listUrlConnection = URL(MylistUrl).openConnection() as HttpURLConnection
        val Projects = mutableListOf<ItemParticipationData>()

        val recyclerView = binding.recyclerView

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val listInputStream = listUrlConnection.inputStream
                val listContent = listInputStream.bufferedReader().use { it.readText().trim() }
                Log.d("please Content", "content $listContent")
                val lines = listContent.split("\n","<br><br>")
                Log.d("please line", "${lines.count()}")
                Log.d("please line", "${lines[0]}")
                Log.d("please line", "${lines[1]}")
                Log.d("please line", "${lines[2]}")
                Log.d("please line", "${lines[3]}")
                var count= AtomicInteger(0)
                for (line in lines) {
                    if (line.isNotEmpty()) {
                        Log.d("please for", line)
                        val ProjectName =
                            line.substringAfter("Project Name:").substringBefore(",").trim()
                        val OrganizationName =
                            line.substringAfter("organization_name:").trim().substringBefore(",")
                                .trim()
                        val Image1 = line.substringAfter("Image1:").substringBefore(",").trim()
                        val Deposit =
                            line.substringAfter("Deposit:").substringBefore(",").trim()
                        val Virtual_Account =
                            line.substringAfter(" Virtual Account:").substringBefore(",").trim()
                        val end_date = line.substringAfter("end_date:").substringBefore(",").trim()
                        val Point =
                            line.substringAfter("Point:").substringBefore(",").trim()
                        val Donation_Limit =
                            line.substringAfter("Donation Limit::").substringBefore(",").trim()

                        Log.d("please title", ProjectName)
                        Log.d("please OrganizationName",OrganizationName)
                        Log.d("please Deposit", Deposit)
                        Log.d("please image1", Image1)
                        Log.d("please Virtual_Account", Virtual_Account)
                        Log.d("please end_date", end_date)
                        Log.d("please point", Point)
                        Log.d("please Donation_Limit", Donation_Limit)

                        Log.d("please cnt", count.getAndIncrement().toString())
                        Projects.add(
                            ItemParticipationData(
                                Image1,ProjectName,OrganizationName,Virtual_Account,Point, Deposit.toInt(), Donation_Limit
                            )
                        )
                    }
                }
                Log.d("please PROJECT", Projects.toString())
                withContext(Dispatchers.Main) {
                    adapter = MyPartiAdapter(requireContext(), childFragmentManager ,Projects)
                    recyclerView.adapter = adapter
                    recyclerView.setCarouselLayoutManager()
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
                listUrlConnection.disconnect()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}