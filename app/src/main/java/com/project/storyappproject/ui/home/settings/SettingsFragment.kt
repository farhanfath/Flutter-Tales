package com.project.storyappproject.ui.home.settings

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.user.UserModel
import com.project.storyappproject.databinding.FragmentSettingsBinding
import com.project.storyappproject.ui.auth.LoginActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private lateinit var userPrefModel : UserPreference
    private lateinit var userModel: UserModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userPrefModel = UserPreference(root.context)
        userModel = userPrefModel.getUser()

        logOutAction()
        languageAction()
        locationAction()
        currentTheme(root.context)
        changeTheme(root.context)
        animationHandler()

        return root
    }

    override fun onResume() {
        super.onResume()
        animationHandler()
    }

    private fun logOutAction() {
        binding.logoutCv.setOnClickListener {
            userPrefModel.removeUser()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun languageAction() {
        binding.languageCv.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun locationAction() {
        binding.locationCv.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context?.startActivity(intent)
        }
    }

    private fun changeTheme(context: Context) {
        val pref = context.let { SettingPreferences.getInstance(it.dataStore) }
        val settingsViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingsViewModel::class.java]

        binding.themeSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun currentTheme(context: Context) {
        val pref = context.let { SettingPreferences.getInstance(it.dataStore) }
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingsViewModel::class.java]

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner
        ) { isDarkModeActive: Boolean ->

            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.themeSwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.themeSwitch.isChecked = false
            }
        }
    }

    private fun animationHandler() {
        binding.apply {

            themeCv.alpha = 0f
            languageCv.alpha = 0f
            logoutCv.alpha = 0f
            locationCv.alpha = 0f

            val themeCvAnimator = ObjectAnimator.ofFloat(themeCv, View.ALPHA, 0f, 1f)
            themeCvAnimator.duration = 700

            val languageCvAnimator = ObjectAnimator.ofFloat(languageCv, View.ALPHA, 0f, 1f)
            languageCvAnimator.duration = 700

            val locationCvAnimator = ObjectAnimator.ofFloat(locationCv, View.ALPHA, 0f, 1f)
            locationCvAnimator.duration = 700

            val logoutCvAnimator = ObjectAnimator.ofFloat(logoutCv, View.ALPHA, 0f, 1f)
            logoutCvAnimator.duration = 700

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(
                themeCvAnimator,
                languageCvAnimator,
                locationCvAnimator,
                logoutCvAnimator
            )

            animatorSet.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}