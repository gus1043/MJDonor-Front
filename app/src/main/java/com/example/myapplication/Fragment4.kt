import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Organization
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RegisterActivity
import com.example.myapplication.databinding.Fragment4Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

class Fragment4 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrgAdapter
    private var _binding: Fragment4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment4Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val MylistUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/organizationInfo.jsp"
        Log.d("please D_url", MylistUrl)
        val listUrlConnection = URL(MylistUrl).openConnection() as HttpURLConnection
        val Organizations = mutableListOf<orgData>()

        val recyclerView = binding.recyclerView

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val listInputStream = listUrlConnection.inputStream
                val listContent = listInputStream.bufferedReader().use { it.readText().trim() }
                Log.d("please Content", "content $listContent")
                val lines = listContent.split("\n","<br>")
                Log.d("please line", "${lines.count()}")
                var count= AtomicInteger(0)
                for (line in lines) {
                    if (line.isNotEmpty()) {
                        Log.d("please for", line)
                        val OrganizationName =
                            line.substringAfter("Organization Name:").trim().substringBefore(",")
                                .trim()
                        val Image = line.substringAfter("Image:").substringBefore(",").trim()

                        Log.d("please OrganizationName",OrganizationName)
                        Log.d("please image", Image)

                        Log.d("please cnt", count.getAndIncrement().toString())
                        Organizations.add(
                            orgData(
                                Image,OrganizationName
                            )
                        )
                    }
                }
                Log.d("please PROJECT", Organizations.toString())
                withContext(Dispatchers.Main) {
                    adapter = OrgAdapter(requireContext() as AppCompatActivity, Organizations)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
