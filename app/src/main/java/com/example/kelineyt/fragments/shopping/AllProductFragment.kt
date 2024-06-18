package com.example.kelineyt.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapters.AllProductAdapter
import com.example.kelineyt.adapters.FavProductAdapter
import com.example.kelineyt.databinding.FragmentAllproductBinding
import com.example.kelineyt.databinding.FragmentSearchBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.VerticalItemDecoration
import com.example.kelineyt.viewmodel.AllProductViewModel
import com.example.kelineyt.viewmodel.FavoriteViewModel
import kotlinx.coroutines.flow.collectLatest


class AllProductFragment: Fragment(R.layout.fragment_allproduct) {
    private lateinit var binding: FragmentAllproductBinding
    private val allAdapter by lazy { AllProductAdapter() }
    private val viewModel by activityViewModels<AllProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllproductBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupFavRv()
        //при нажатии на товар переход на карточку деталей товара
        allAdapter.onProductClick = {
            val b = Bundle().apply {
                putParcelable("product", it)
            }
            findNavController().navigate(R.id.action_allProductFragment_to_productDetailsFragment, b)
        }
        binding.imageClose.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.allProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
//                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
//                        binding.progressbarCart.visibility = View.INVISIBLE


                            allAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error -> {
//                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun hideOtherViews() {
        binding.apply {
            AllRecycle.visibility = View.GONE
        }
    }
//настройка карточек товаров
    private fun setupFavRv() {

        binding.AllRecycle.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = allAdapter
            addItemDecoration(VerticalItemDecoration())
            val recyclerView: RecyclerView = findViewById(com.example.kelineyt.R.id.AllRecycle)
            val spaceHeight = 15 // Высота отступов в пикселях
            recyclerView.addItemDecoration(SpaceItemDecoration(spaceHeight))
        }
    }

}