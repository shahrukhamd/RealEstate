package com.shahrukhamd.realestate.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shahrukhamd.realestate.databinding.FragmentRealEstateDetailsBinding

class RealEstateDetails: Fragment() {

    private val args: RealEstateDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentRealEstateDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            property = args.property
        }

        return viewBinding.root
    }
}