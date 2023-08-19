import android.content.Intent
import android.os.Bundle
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

class Fragment3 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPartiAdapter
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

        // Sample data for the RecyclerView items
        val itemList = listOf(
            ItemParticipationData("https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png", "아메리칸 스나이퍼", "유니세프 한국위원회", "카카오뱅크 3333-58-481", "23/08/28 19:00", 8555 , true),
            ItemParticipationData("https://www.kasandbox.org/programming-images/avatars/purple-pi-pink.png", "아메리칸 스나이퍼", "유니세프 한국위원회", "카카오뱅크 3333-58-481", "23/08/28 19:00", 8555, false ),
            ItemParticipationData("https://www.kasandbox.org/programming-images/avatars/primosaur-ultimate.png", "아메리칸 스나이퍼", "유니세프 한국위원회", "카카오뱅크 3333-58-481", "23/08/28 19:00", 8555 , true),
        )

        // Pass the FragmentManager as a parameter to the adapter
        adapter = MyPartiAdapter(requireContext(), childFragmentManager, itemList)
        recyclerView.adapter = adapter

        val dummyUserData = UserData(
            name = "최지현",
            studentNumber = "60211704",
            donationCount = 3,
            totalDonation = "3,000,000원",
            profile="https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png"
        )
        binding.name.text = dummyUserData.name
        binding.stdnum.text = dummyUserData.studentNumber
        binding.doncount.text = dummyUserData.donationCount.toString()
        binding.totaldon.text = dummyUserData.totalDonation
        Glide.with(this)
            .load(dummyUserData.profile)
            .into(binding.profile)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
