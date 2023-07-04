package com.mihaiim.runningtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.mihaiim.runningtracker.R
import com.mihaiim.runningtracker.databinding.FragmentSetupBinding
import com.mihaiim.runningtracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.mihaiim.runningtracker.other.Constants.KEY_NAME
import com.mihaiim.runningtracker.other.Constants.KEY_WEIGHT
import com.mihaiim.runningtracker.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : BaseFragment<FragmentSetupBinding>(FragmentSetupBinding::inflate) {
    @Inject
    lateinit var sharedPref: SharedPreferences
    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
            savedInstanceState,
            navOptions)
        }

        binding.tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    "Please enter all the fields",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()

        (requireActivity() as? MainActivity)?.setToolbarTitle("Let's go, $name!")
        return true
    }
}