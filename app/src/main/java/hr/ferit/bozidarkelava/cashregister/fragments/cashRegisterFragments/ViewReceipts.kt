package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipts
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentViewReceiptsBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.interfaces.ReceiptButtonClick
import hr.ferit.bozidarkelava.cashregister.recyclerViews.viewReceiptsRecyclerView.ViewReceiptsRecyclerAdapter
import java.io.File


class ViewReceipts : Fragment(), MVVM {

    private lateinit var binding: FragmentViewReceiptsBinding
    private lateinit var manager: Manager
    private lateinit var adapter: ViewReceiptsRecyclerAdapter

    private val databaseReceipts = CashRegisterDatabase.getInstance().receiptsDao()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_receipts, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun setUpFragment() {
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_view_receipts)
        manager = activity as Manager
    }

    override fun setUpBinding() {

        val clicks = createClicks()

        binding.btnBack.setOnClickListener() {
            manager.openFragment(R.id.frameViewReceipts, MainMenu())
        }

        binding.rvViewReceipts.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = ViewReceiptsRecyclerAdapter(databaseReceipts.selectAll() as MutableList<Receipts>, clicks, CashRegisterApp.ApplicationContext)
        binding.rvViewReceipts.adapter = adapter
    }

    private fun createClicks(): ReceiptButtonClick {
        val mClick = object : ReceiptButtonClick {
            override fun openReceipt(position: Int) {

                //Log.d("PATH", databaseReceipts.selectPathFromId(position+1).toString())
                val file: File = File(databaseReceipts.selectPathFromId(position+1))
                val intent = Intent(Intent.ACTION_VIEW)
                    .setDataAndType(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                            CashRegisterApp.ApplicationContext,
                            "hr.ferit.android.fileprovider",
                            file!!
                        ) else Uri.fromFile(file), "application/pdf"
                    ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        }
        return  mClick
    }
}