import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.databinding.Fragment1Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

class Fragment1 : Fragment() {
    private lateinit var recyclerViewmain: RecyclerView
    private lateinit var adapter: CarouselAdapter
    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/projectInfo.jsp"
        Log.d("please D_url", listUrl)
        val listUrlConnection = URL(listUrl).openConnection() as HttpURLConnection
        val Projects = mutableListOf<CarouselItem>()

        val recyclerViewmain = binding.recyclerViewmain

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val listInputStream = listUrlConnection.inputStream
                val listContent = listInputStream.bufferedReader().use { it.readText().trim() }
                Log.d("please Content", "content $listContent")
                val lines = listContent.split("\n","<br>")
                Log.d("please line", "${lines.count()}")
                Log.d("please line", "${lines[0]}")
                Log.d("please line", "${lines[1]}")
                Log.d("please line", "${lines[2]}")
                Log.d("please line", "${lines[3]}")
                var count= AtomicInteger(0)
                for (line in lines) {
                    if (line.isNotEmpty()) {
                        Log.d("please for", line)
                        val p_id = line.substringAfter("p_id:").substringBefore(",").trim()
                        val ProjectName =
                            line.substringAfter("Project Name:").substringBefore(",").trim()
                        val OrganizationName =
                            line.substringAfter("organization_name:").trim().substringBefore(",")
                                .trim()
                        val Image1 = line.substringAfter("Image1:").substringBefore(",").trim()
                        val Image2 = line.substringAfter("Image2:").substringBefore(",").trim()
                        val DESCRIPTION =
                            line.substringAfter("DESCRIPTION:").substringBefore(",").trim()
                        val start_date =
                            line.substringAfter("start_date:").substringBefore(",").trim()
                        val end_date = line.substringAfter("end_date:").substringBefore(",").trim()
                        val TargetPoint =
                            line.substringAfter("Target Point:").substringBefore(",").trim()
                        val current_point =
                            line.substringAfter("current_point:").substringBefore(",").trim()

                        Log.d("please p_id", p_id)
                        Log.d("please title", ProjectName)
                        Log.d("please OrganizationName", OrganizationName)
                        Log.d("please image1", Image1)
                        Log.d("please image2", Image2)
                        Log.d("please DESCRIPTION", DESCRIPTION)
                        Log.d("please start_date", start_date)
                        Log.d("please end_date", end_date)
                        Log.d("please targetpoint", TargetPoint)
                        Log.d("please current_point", current_point)

                        Log.d("please cnt", count.getAndIncrement().toString())
                        Projects.add(
                            CarouselItem(
                                Image1,
                                Image2,
                                p_id.toInt(),
                                ProjectName,
                                DESCRIPTION,
                                OrganizationName,
                                end_date,
                                TargetPoint,
                                current_point
                            )
                        )

                    }
                }
                Log.d("please PROJECT", Projects.toString())
                withContext(Dispatchers.Main) {
                    adapter = CarouselAdapter(requireContext(), Projects)
                    recyclerViewmain.adapter = adapter
                    recyclerViewmain.setCarouselLayoutManager()
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
