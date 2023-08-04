import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.SignupActivity
import com.example.myapplication.databinding.Fragment3Binding

class Fragment3 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private var _binding: Fragment3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemList = listOf(
            ItemData(R.drawable.img1, "아메리칸 스나이퍼", "유니세프 한국위원회"),
            ItemData(R.drawable.img3, "아메리칸 스나이퍼", "유니세프 한국위원회"),
            ItemData(R.drawable.img2, "아메리칸 스나이퍼", "유니세프 한국위원회"),
        )

        adapter = MyAdapter(itemList)
        recyclerView.adapter = adapter

        binding.login.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signup.setOnClickListener {
            val intent = Intent(requireContext(), SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
