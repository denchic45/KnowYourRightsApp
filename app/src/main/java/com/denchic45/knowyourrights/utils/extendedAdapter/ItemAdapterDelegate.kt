package com.denchic45.widget.extendedAdapter

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import com.denchic45.knowyourrights.databinding.ItemIconContentBinding
import com.denchic45.knowyourrights.domain.model.ListItem
import com.denchic45.knowyourrights.ui.adapter.BaseViewHolder
import com.denchic45.knowyourrights.utils.viewBinding

class ItemAdapterDelegate : ListItemAdapterDelegate<ListItem, ItemAdapterDelegate.ItemHolder>() {

    class ItemHolder(
        itemIconContentBinding: ItemIconContentBinding,
    ) : BaseViewHolder<ListItem, ItemIconContentBinding>(
        itemIconContentBinding
    ) {
        override fun onBind(item: ListItem) {
            with(binding) {
                item.icon.fold({
                    ivIcon.setImageResource(it)
                }, {
                    val iconResId = itemView.context.resources.getIdentifier(
                        it,
                        "drawable",
                        itemView.context.packageName
                    )
                    ivIcon.setImageResource(iconResId)
                })
                tvName.text = item.title

                item.color.fold({
                    if (it != 0) ImageViewCompat.setImageTintList(
                        ivIcon,
                        ColorStateList.valueOf(it)
                    )
                }, {
                    val identifier = itemView.resources
                        .getIdentifier(it, "color", itemView.context.packageName)
                    ColorStateList.valueOf(
                        identifier
                    )
                })
            }
        }
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ListItem && item.type == 0
    }

    override fun onBindViewHolder(item: ListItem, holder: ItemHolder) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): ItemHolder {
        return ItemHolder(parent.viewBinding(ItemIconContentBinding::inflate))
    }
}