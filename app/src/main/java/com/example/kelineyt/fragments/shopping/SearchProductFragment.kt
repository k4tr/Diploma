package com.example.kelineyt.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapters.AllProductAdapter
import com.example.kelineyt.adapters.ProductAdapter
import com.example.kelineyt.adapters.ProductsAdapter
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.FragmentSearchproductBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.VerticalItemDecoration
import com.example.kelineyt.viewmodel.SearchProductViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale


class SearchProductFragment: Fragment(R.layout.fragment_searchproduct) {
    private lateinit var binding: FragmentSearchproductBinding
    private val viewModel by activityViewModels<SearchProductViewModel>()
    private val allAdapter by lazy { ProductsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchproductBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        setupFavRv()

        allAdapter.onProductClick = {
            val b = Bundle().apply {
                putParcelable("product", it)
            }
            findNavController().navigate(R.id.action_searchProduct_to_productDetailsFragment, b)

        }


        binding.productRecycle.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productRecycle.adapter = allAdapter
        binding.etSearch.addTextChangedListener { text ->
            viewModel.filterProducts(text.toString())
        }
        lifecycleScope.launchWhenStarted {
            viewModel.filteredProducts.collectLatest { products ->
                allAdapter.differ.submitList(products)
            }
        }
    }

    private fun setupFavRv() {

        binding.productRecycle.apply {
            layoutManager = GridLayoutManager(context, 2)

            addItemDecoration(VerticalItemDecoration())
            val recyclerView: RecyclerView = findViewById(com.example.kelineyt.R.id.productRecycle)
            val spaceHeight = 15 // Высота отступов в пикселях
            recyclerView.addItemDecoration(SpaceItemDecoration(spaceHeight))
        }
    }
}