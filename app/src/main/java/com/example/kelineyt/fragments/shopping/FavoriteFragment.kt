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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapters.FavProductAdapter
import com.example.kelineyt.databinding.FragmentSearchBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.VerticalItemDecoration
import com.example.kelineyt.viewmodel.FavoriteViewModel
import kotlinx.coroutines.flow.collectLatest


class FavoriteFragment: Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val favAdapter by lazy { FavProductAdapter() }
    private val viewModel by activityViewModels<FavoriteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupFavRv()
        //при нажатии на товар переход на карточку деталей товара
        favAdapter.onProductClick = {
            val b = Bundle().apply {
                putParcelable("product", it.product)
            }
            findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment, b)
        }
        binding.imageClose.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.favProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
//                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
//                        binding.progressbarCart.visibility = View.INVISIBLE


                            favAdapter.differ.submitList(it.data)

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
            FavRecycle.visibility = View.GONE
        }
    }
//настройка карточек в избранных товарах
    private fun setupFavRv() {

        binding.FavRecycle.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favAdapter
            addItemDecoration(VerticalItemDecoration())
            val recyclerView: RecyclerView = findViewById(com.example.kelineyt.R.id.FavRecycle)
            val spaceHeight = 15 // Высота отступов в пикселях
            recyclerView.addItemDecoration(SpaceItemDecoration(spaceHeight))
        }
    }

}