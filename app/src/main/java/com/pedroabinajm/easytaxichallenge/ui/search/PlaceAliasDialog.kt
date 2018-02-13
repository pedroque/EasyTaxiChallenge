package com.pedroabinajm.easytaxichallenge.ui.search

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.pedroabinajm.easytaxichallenge.R
import kotlinx.android.synthetic.main.dialog_place_alias.*


class PlaceAliasDialog : DialogFragment() {
    var onPositiveButtonClickListener: (dialog: Dialog, alias: String) -> Unit = { _: Dialog, _: String -> }
    var onNegativeButtonClickListener: (dialog: Dialog) -> Unit = {}

    companion object {
        @JvmStatic
        private fun newInstance(): PlaceAliasDialog {
            return PlaceAliasDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_place_alias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positive_button.setOnClickListener {
            if (alias_text.length() > 0) {
                onPositiveButtonClickListener(dialog, alias_text.text.toString())
                dialog.dismiss()
            } else {
                Toast.makeText(view.context, R.string.invalid_place_alias, Toast.LENGTH_SHORT).show()
            }
        }
        negative_button.setOnClickListener {
            onNegativeButtonClickListener(dialog)
            dialog.dismiss()
        }
    }

    class Builder {
        private var onPositiveButtonClickListener: (dialog: Dialog, alias: String) -> Unit = { _: Dialog, _: String -> }
        private var onNegativeButtonClickListener: (dialog: Dialog) -> Unit = {}


        fun positiveButton(onPositiveButtonClickListener: (dialog: Dialog, alias: String) -> Unit): Builder {
            this.onPositiveButtonClickListener = onPositiveButtonClickListener
            return this
        }

        fun negativeButton(onNegativeButtonClickListener: (dialog: Dialog) -> Unit): Builder {
            this.onNegativeButtonClickListener = onNegativeButtonClickListener
            return this
        }

        fun show(fragmentManager: FragmentManager) {
            val dialog = PlaceAliasDialog.newInstance()
            dialog.onNegativeButtonClickListener = onNegativeButtonClickListener
            dialog.onPositiveButtonClickListener = onPositiveButtonClickListener
            dialog.show(fragmentManager, "mira_dialog")
        }
    }
}