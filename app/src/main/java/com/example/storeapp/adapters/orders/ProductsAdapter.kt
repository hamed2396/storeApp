
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.root.logError
import com.example.storeapp.databinding.ItemOrderProductBinding
import com.example.storeapp.models.profile.ResponseOrders
import com.example.storeapp.utils.Constants.BASE_URL_IMAGE
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import javax.inject.Inject

class ProductsAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var items = emptyList<ResponseOrders.Data.OrderItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemOrderProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseOrders.Data.OrderItem) {
            binding.apply {
                item.extras?.let {
                    itemTitle.text = it.title
                    //Image
                    val imageUrl = "$BASE_URL_IMAGE${it.image}"
                    itemImg.loadImage(imageUrl)
                }
            }
        }
    }

    fun setData(data: List<ResponseOrders.Data.OrderItem>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        result.dispatchUpdatesTo(this)
    }
}