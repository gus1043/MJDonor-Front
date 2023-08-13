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
import com.example.myapplication.databinding.Fragment1Binding
import com.example.myapplication.databinding.Fragment3Binding

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

        recyclerViewmain = binding.recyclerViewmain

        // Sample data for the RecyclerView items
        val itemList = listOf(
            ItemData(R.drawable.img1, "아메리칸 스나이퍼", "유니세프 한국위원회","글로벌"),
            ItemData(R.drawable.img3, "아메리칸 스나이퍼", "유니세프 한국위원회","환경"),
            ItemData(R.drawable.img2, "아메리칸 스나이퍼", "유니세프 한국위원회","한부모가정"),
        )

        adapter = CarouselAdapter(requireContext(), itemList)
        recyclerViewmain.adapter = adapter

        // Set up the carousel layout manager for the RecyclerView
        recyclerViewmain.setCarouselLayoutManager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
