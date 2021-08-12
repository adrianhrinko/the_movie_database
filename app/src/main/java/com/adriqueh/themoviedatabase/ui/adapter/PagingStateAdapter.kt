package com.adriqueh.themoviedatabase.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adriqueh.themoviedatabase.R
import com.adriqueh.themoviedatabase.databinding.ItemPagingStateBinding

class PagingStateAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val adapter: PagingDataAdapter<T, VH>
) : LoadStateAdapter<PagingStateAdapter.PagingStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        PagingStateItemViewHolder(
            ItemPagingStateBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_paging_state, parent, false)
            )
        ) { adapter.retry() }

    override fun onBindViewHolder(holder: PagingStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class PagingStateItemViewHolder(
        private val binding: ItemPagingStateBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retryCallback() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}