package com.example.weatherapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.weatherapp.*
import com.example.weatherapp.data.Language
import com.example.weatherapp.data.NightModeType
import com.example.weatherapp.data.PreferencesManager
import com.example.weatherapp.data.UnitsType
import com.example.weatherapp.databinding.SettingsFragmentBinding
import kotlinx.android.synthetic.main.settings_fragment.*
import javax.inject.Inject


class SettingsFragment : Fragment() {

    @Inject
    lateinit var preferencesManager: PreferencesManager
    private lateinit var binding: SettingsFragmentBinding

    private var nigthModeChooseItem = 0
    private var unitsTypeChooseItem = 0
    private var languageChooseItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        App.get(requireContext()).applicationComponent.inject(this)
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        val savedNightMode = preferencesManager.getSavedNightModeValue()
        nigthModeChooseItem = NightModeType.fromValue(savedNightMode).ordinal


        val savedUnitsType = preferencesManager.getSavedUnitsValue()
        unitsTypeChooseItem = UnitsType.fromValue(savedUnitsType!!).ordinal


        val savedLanguage = preferencesManager.getSavedLanguage()
        languageChooseItem = Language.fromValue(savedLanguage).ordinal


        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            nightmode = getString(NightModeType.fromValue(savedNightMode).title)
            units = getString(UnitsType.fromValue(savedUnitsType).title)
            language = getString(Language.fromValue(savedLanguage).title)

            nigthModSelectorLl.setOnClickListener {
                showNightModeAlertDialog()
            }
            unitsTypeSelectorLl.setOnClickListener {
                showUnitsTypeAlertDialog()
            }
            languageSelectorLl.setOnClickListener {
                showLanguageSelectorAlertDialog()
            }
        }.root

    }


    private fun showLanguageSelectorAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val items = arrayOf(
            getString(Language.ENGLISH.title),
            getString(Language.RUSSIAN.title),
            getString(Language.BELARUSIAN.title),
            getString(Language.UKRAINIAN.title)
        )

        alertDialog.setTitle(getString(R.string.language))

        alertDialog.setSingleChoiceItems(
            items,
            languageChooseItem,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val language = Language.fromCustomOrdinal(which)

                    languageChooseItem = language.ordinal

                    languageSelector_tv.text = getString(language.title)

                    preferencesManager.saveLanguageValue(language.value)
                    preferencesManager.saveCountry(language.country)

                    activity?.recreate()
                    dialog!!.cancel()
                }
            })
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    private fun showNightModeAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.nightMode))

        val items = mutableListOf(
            getString(NightModeType.MODE_NIGHT_NO.title),
            getString(NightModeType.MODE_NIGHT_YES.title)
        )

        if (NightModeType.getDefaultMode() == NightModeType.MODE_NIGHT_FOLLOW_SYSTEM) {
            items.add(getString(NightModeType.getDefaultMode().title))
        }


        alertDialog.setSingleChoiceItems(
            items.toTypedArray(),
            nigthModeChooseItem,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val nightMode = NightModeType.fromCustomOrdinal(which)

                    nigthModeChooseItem = nightMode.ordinal

                    nigthModSelector_tv.text = getString(nightMode.title)

                    preferencesManager.saveNightModeValue(nightMode.value)

                    AppCompatDelegate.setDefaultNightMode(nightMode.value)

                    dialog!!.cancel()
                }
            })
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }


    private fun showUnitsTypeAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.units))

        val items = arrayOf(
            getString(UnitsType.METRIC_UNITS.title),
            getString(UnitsType.IMPERIAL_UNITS.title),
            getString(UnitsType.ABSOLUTE_UNITS.title)
        )

        alertDialog.setSingleChoiceItems(
            items,
            unitsTypeChooseItem,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val unitsType = UnitsType.fromCustomOrdinal(which)

                    unitsTypeChooseItem = unitsType.ordinal

                    unitsTypeSelector_tv.text = getString(unitsType.title)

                    preferencesManager.saveUnitsValue(unitsType.value)

                    dialog!!.cancel()
                }
            })
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

}

