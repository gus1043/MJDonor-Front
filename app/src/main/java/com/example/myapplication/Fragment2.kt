import android.content.Intent
import android.os.Bundle
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
import com.example.myapplication.databinding.Fragment2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

class Fragment2 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private var _binding: Fragment2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val listUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/donationList.jsp"
        Log.d("please D_url", listUrl)
        val listUrlConnection = URL(listUrl).openConnection() as HttpURLConnection
        val Projects = mutableListOf<ItemData>()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
                        val end_date = line.substringAfter("end_date:").substringBefore(",").trim()
                        val TargetPoint =
                            line.substringAfter("Target Point:").substringBefore(",").trim()
                        val current_point =
                            line.substringAfter("current_point:").substringBefore(",").trim()
                        val type =
                            line.substringAfter("start_date:").substringBefore(",").trim()

                        Log.d("please p_id", p_id)
                        Log.d("please title", ProjectName)
                        Log.d("please OrganizationName", OrganizationName)
                        Log.d("please image1", Image1)
                        Log.d("please image2", Image2)
                        Log.d("please DESCRIPTION", DESCRIPTION)
                        Log.d("please type", type)
                        Log.d("please end_date", end_date)
                        Log.d("please targetpoint", TargetPoint)
                        Log.d("please current_point", current_point)

                        Log.d("please cnt", count.getAndIncrement().toString())
                        Projects.add(
                            ItemData(
                                Image1,
                                Image2,
                                p_id.toInt(),
                                ProjectName,
                                DESCRIPTION,
                                OrganizationName,
                                end_date,
                                TargetPoint,
                                current_point,
                                type
                            )
                        )

                    }
                }
                Log.d("please PROJECT", Projects.toString())
                withContext(Dispatchers.Main) {
                    adapter = MyAdapter(requireContext() as AppCompatActivity, Projects)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
                listUrlConnection.disconnect()
            }
        }


        binding.all.setOnClickListener {
            adapter.updateData(Projects)
        }

        binding.environment.setOnClickListener {
            val filteredItemList = Projects.filter { it.type == "환경" }
            adapter.updateData(filteredItemList)
        }

        binding.singleParent.setOnClickListener {
            val filteredItemList = Projects.filter { it.type == "한부모가정" }
            adapter.updateData(filteredItemList)
        }

        binding.lowIncome.setOnClickListener {
            val filteredItemList = Projects.filter { it.type == "저소득층" }
            adapter.updateData(filteredItemList)
        }

        binding.global.setOnClickListener {
            val filteredItemList = Projects.filter { it.type == "글로벌" }
            adapter.updateData(filteredItemList)
        }
        binding.donBtn.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
