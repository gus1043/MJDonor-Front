import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.databinding.Fragment1Binding

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
            CarouselItem("https://placebear.com/g/200/200","https://placebear.com/g/200/200",1, "아메리칸 스나이퍼1", "어쩌구 저쩌구 와앙","유니세프 한국위원회1",20230529,6000000,10000),
            CarouselItem("https://cdn.shopify.com/s/files/1/1830/5085/products/VE0007_BCAA_Capsule_90ct_2048x2048.png?v=1494855182","https://cdn.shopify.com/s/files/1/1830/5085/products/VE0007_BCAA_Capsule_90ct_2048x2048.png?v=1494855182", 2,"아메리칸 스나이퍼2", "ㄹㅇㄹㄴㄹㅇㄴㄹㄴㄹㅇ","유니세프 한국위원회2",20230829,8000000,100),
            CarouselItem("https://source.unsplash.com/user/c_v_r/1900×800","https://source.unsplash.com/user/c_v_r/1900×800", 3,"아메리칸 스나이퍼3", "ㄹㅇㄴㄹㅇㄴㄹㅇㄴㄹㅇㄴㄹㄴ","유니세프 한국위원회2",20230729,90000000,9023343),
        )

        adapter = CarouselAdapter(requireContext(), itemList)
        recyclerViewmain.adapter = adapter

        recyclerViewmain.setCarouselLayoutManager()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
