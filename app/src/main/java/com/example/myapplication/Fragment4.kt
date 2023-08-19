import android.content.Intent
import android.os.Bundle
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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data for the RecyclerView items
        val itemList = listOf(
            orgData("https://www.kasandbox.org/programming-images/avatars/spunky-sam.png", 1, "유니세프 한국위원회"),
            orgData("https://www.kasandbox.org/programming-images/avatars/spunky-sam-green.png", 3,"유니세프 한국위원회"),
            orgData("https://www.kasandbox.org/programming-images/avatars/purple-pi.png", 5,"유니세프 한국위원회"),
        )

        adapter = OrgAdapter(requireContext() as AppCompatActivity, itemList)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
