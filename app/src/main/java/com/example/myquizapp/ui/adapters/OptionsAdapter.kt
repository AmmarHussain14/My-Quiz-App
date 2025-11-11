package com.example.myquizapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.R

class OptionsAdapter(
    private val onOptionClick: (Int) -> Unit
) : ListAdapter<OptionItem, OptionsAdapter.OptionViewHolder>(OptionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_option, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OptionViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val optionIndicator: TextView = itemView.findViewById(R.id.optionIndicator)
        private val optionText: TextView = itemView.findViewById(R.id.optionText)
        private val cardView: CardView = itemView as CardView

        fun bind(option: OptionItem) {
            optionIndicator.text = option.letter
            optionText.text = option.text
            
            // Set background based on state
            when {
                option.isCorrect -> {
                    cardView.setCardBackgroundColor(itemView.context.getColor(R.color.quiz_success_container))
                    optionIndicator.setBackgroundResource(R.drawable.option_indicator_correct)
                    optionIndicator.setTextColor(itemView.context.getColor(android.R.color.white))
                }
                option.isWrong -> {
                    cardView.setCardBackgroundColor(itemView.context.getColor(R.color.quiz_error_container))
                    optionIndicator.setBackgroundResource(R.drawable.option_indicator_wrong)
                    optionIndicator.setTextColor(itemView.context.getColor(android.R.color.white))
                }
                option.isSelected -> {
                    cardView.setCardBackgroundColor(itemView.context.getColor(R.color.quiz_secondary_container))
                    optionIndicator.setBackgroundResource(R.drawable.option_indicator_default)
                    optionIndicator.setTextColor(itemView.context.getColor(R.color.quiz_on_surface))
                }
                else -> {
                    cardView.setCardBackgroundColor(itemView.context.getColor(R.color.quiz_surface))
                    optionIndicator.setBackgroundResource(R.drawable.option_indicator_default)
                    optionIndicator.setTextColor(itemView.context.getColor(R.color.quiz_on_surface))
                }
            }
            
            // Set text color based on state
            val textColor = when {
                option.isCorrect -> itemView.context.getColor(R.color.quiz_on_success_container)
                option.isWrong -> itemView.context.getColor(R.color.quiz_on_error_container)
                option.isSelected -> itemView.context.getColor(R.color.quiz_on_secondary_container)
                else -> itemView.context.getColor(R.color.quiz_on_surface)
            }
            optionText.setTextColor(textColor)
            
            // Set card elevation based on state
            cardView.cardElevation = if (option.isCorrect || option.isWrong || option.isSelected) 8f else 2f
            
            itemView.setOnClickListener {
                onOptionClick(option.index)
            }
        }
    }

    class OptionDiffCallback : DiffUtil.ItemCallback<OptionItem>() {
        override fun areItemsTheSame(oldItem: OptionItem, newItem: OptionItem): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(oldItem: OptionItem, newItem: OptionItem): Boolean {
            return oldItem == newItem
        }
    }
}
