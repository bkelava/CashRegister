package hr.ferit.bozidarkelava.cashregister.interfaces

import androidx.fragment.app.Fragment

interface Manager {

    fun openFragment(layoutID: Int, fragment: Fragment)
}