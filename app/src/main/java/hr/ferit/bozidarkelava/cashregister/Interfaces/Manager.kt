package hr.ferit.bozidarkelava.cashregister.Interfaces

import androidx.fragment.app.Fragment

interface Manager {

    fun openFragment(layoutID: Int, fragment: Fragment)
}