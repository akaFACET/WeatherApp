package com.example.weatherapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.weatherapp.*
import com.example.weatherapp.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.settings_fragment.*


class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var preferencesManager: PreferencesManager

    private var nigthModeChooseItem = 0
    private var unitsTypeChooseItem = 0
    private var languageChooseItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateSettingsFragment")
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        val savedNightMode = preferencesManager.getSavedNightModeValue()
        nigthModeChooseItem = NightModeType.fromValue(savedNightMode).ordinal
        nigthModSelector_tv.text = getString(NightModeType.fromValue(savedNightMode).title)

        val savedUnitsType = preferencesManager.getSavedUnitsValue()
        unitsTypeChooseItem = UnitsType.fromValue(savedUnitsType!!).ordinal
        unitsTypeSelector_tv.text = getString(UnitsType.fromValue(savedUnitsType).title)

        val savedLanguage = preferencesManager.getSavedLanguage()
        languageChooseItem = Language.fromValue(savedLanguage).ordinal
        languageSelector_tv.text = getString(Language.fromValue(savedLanguage).title)


        nigthModSelector_ll.setOnClickListener{
            showNightModeAlertDialog()
        }

        unitsTypeSelector_ll.setOnClickListener {
            showUnitsTypeAlertDialog()
        }

        languageSelector_ll.setOnClickListener {
            showLanguageSelectorAlertDialog()
        }

    }



    private fun showLanguageSelectorAlertDialog(){
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
                    //update configuration
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

        val items = arrayOf(
            getString(NightModeType.MODE_NIGHT_NO.title),
            getString(NightModeType.MODE_NIGHT_YES.title),
            getString(NightModeType.getDefaultMode().title)
        )

        alertDialog.setSingleChoiceItems(
            items,
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


    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachSettingsFragment")
    }

}

