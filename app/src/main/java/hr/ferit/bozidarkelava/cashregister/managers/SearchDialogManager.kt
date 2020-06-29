package hr.ferit.bozidarkelava.cashregister.managers

import ir.mirrajabi.searchdialog.core.Searchable

class SearchDialogManager(title: String): Searchable {

    private var title: String

    init {
        this.title = title
    }

    fun setTitle(text: String) {
        this.title = text
    }

    override fun getTitle(): String {
        return title
    }
}