package com.shahrukhamd.realestate.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.shahrukhamd.realestate.databinding.FragmentRealEstateListBinding
import com.shahrukhamd.realestate.ui.RealEstateViewModel
import com.shahrukhamd.realestate.ui.base.EventObserver

class RealEstateListFragment: Fragment() {

    private val viewModel: RealEstateViewModel by activityViewModels()
    private var listAdapter: PropertyListAdapter? = null

    private lateinit var viewBinding: FragmentRealEstateListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRealEstateListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()

        viewBinding.srPropertyList.setOnRefreshListener {
            viewModel.onPropertyListRefresh()
        }
    }

    private fun initRecyclerView() {
        listAdapter = PropertyListAdapter(viewModel)
        viewBinding.rvPropertyList.adapter = listAdapter
    }

    private fun initObserver() {
        viewModel.showProperties.observe(viewLifecycleOwner) {
            viewBinding.srPropertyList.isRefreshing = false
            viewBinding.rvPropertyList.visibility = View.VISIBLE
            listAdapter?.submitList(it)
        }

        viewModel.showError.observe(viewLifecycleOwner, EventObserver {
            viewBinding.srPropertyList.isRefreshing = false
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.showLoading.observe(viewLifecycleOwner, EventObserver {
            viewBinding.srPropertyList.isRefreshing = it
        })

        viewModel.showPropertyDetails.observe(viewLifecycleOwner, EventObserver { property ->
            findNavController().navigate(
                RealEstateListFragmentDirections.actionListFragmentToDetailsFragment(property)
            )
        })
    }
}