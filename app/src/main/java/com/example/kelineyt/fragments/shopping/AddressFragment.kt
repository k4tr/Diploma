package com.example.kelineyt.fragments.shopping

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kelineyt.data.Address
import com.example.kelineyt.databinding.FragmentAddressBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.hideBottomNavigationView
import com.example.kelineyt.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    val args by navArgs<AddressFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.imageAddressClose.setOnClickListener{
            findNavController().navigate(com.example.kelineyt.R.id.profileFragment)
        }
        val address = args.address
        binding.buttonDelelte.setOnClickListener{
            lifecycleScope.launch{
                viewModel.removeAdress(address!!.addressTitle)
                findNavController().navigateUp()
            }
        }
        super.onViewCreated(view, savedInstanceState)
        // Настройка Spinner
        val spinner: Spinner = binding.edState
        val states = arrayOf("Выберите район", "Адмиралтейский", "Василеостровский", "Выборгский", "Калининский", "Кировский", "Колпинский","Красногвардейский","Красносельский","Центральный","Фрунзенский","Приморский","Петроградский","Невский","Московский")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, states)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (address == null) {
            binding.buttonDelelte.visibility = View.GONE
        } else {
            binding.apply {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edStreet.setText(address.street)
                edPhone.setText(address.phone)
                edCity.setText(address.city)
                // Установите выбранное состояние в Spinner
                val statePosition = adapter.getPosition(address.state)
                spinner.setSelection(statePosition)
            }
        }
        spinner.setSelection(0)
        binding.apply {
            buttonSave?.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = spinner.selectedItem.toString() // Получите выбранное значение из Spinner
                if (state == "Выберите район") {
                    Toast.makeText(requireContext(), "Пожалуйста, выберите район", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedAddress = Address(id = args.address?.id ?: "", addressTitle, fullName, street, phone, city, state)
                    viewModel.saveOrUpdateAddress(updatedAddress)

                }
            }
        }


    }

}