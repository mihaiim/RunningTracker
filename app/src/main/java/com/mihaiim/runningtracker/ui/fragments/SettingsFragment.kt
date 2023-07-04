package com.mihaiim.runningtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.mihaiim.runningtracker.databinding.FragmentSettingsBinding
import com.mihaiim.runningtracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.mihaiim.runningtracker.other.Constants.KEY_NAME
import com.mihaiim.runningtracker.other.Constants.KEY_WEIGHT
import com.mihaiim.runningtracker.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldsFromSharedPref()
        binding.btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                Snackbar.make(
                    view,
                    "Saved changes",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    view,
                    "Please enter all the fields",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPref.getString(KEY_NAME, "") ?: ""
        val weight = sharedPref.getFloat(KEY_WEIGHT, 80f)
        binding.etName.setText(name)
        binding.etWeight.setText(weight.toString())
    }

    private fun applyChangesToSharedPref(): Boolean {
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