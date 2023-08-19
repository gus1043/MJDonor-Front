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
import com.example.myapplication.databinding.Fragment2Binding

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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data for the RecyclerView items
        val itemList = listOf(
            ItemData("https://www.kasandbox.org/programming-images/avatars/duskpin-tree.png","https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png",1, "아메리칸 스나이퍼9", "어쩌구 저쩌구 와앙","유니세프 한국위원회1",20230529,6000000,10000,"환경"),
            ItemData("https://www.kasandbox.org/programming-images/avatars/marcimus-orange.png", "https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png",2,"아메리칸 스나이퍼21", "ㄹㅇㄹㄴㄹㅇㄴㄹㄴㄹㅇ","유니세프 한국위원회2",20230829,8000000,100,"글로벌"),
            ItemData("https://www.kasandbox.org/programming-images/avatars/duskpin-sapling.png", "https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png",3,"아메리칸 스나이퍼3", "ㄹㅇㄴㄹㅇㄴㄹㅇㄴㄹㅇㄴㄹㄴ","유니세프 한국위원회23",20230729,90000000,9023343,"저소득층"),
        )

        adapter = MyAdapter(requireContext() as AppCompatActivity, itemList)
        recyclerView.adapter = adapter

        binding.all.setOnClickListener {
            adapter.updateData(itemList)
        }

        binding.environment.setOnClickListener {
            val filteredItemList = itemList.filter { it.type == "환경" }
            adapter.updateData(filteredItemList)
        }

        binding.singleParent.setOnClickListener {
            val filteredItemList = itemList.filter { it.type == "한부모가정" }
            adapter.updateData(filteredItemList)
        }

        binding.lowIncome.setOnClickListener {
            val filteredItemList = itemList.filter { it.type == "저소득층" }
            adapter.updateData(filteredItemList)
        }

        binding.global.setOnClickListener {
            val filteredItemList = itemList.filter { it.type == "글로벌" }
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
