package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import android.view.View
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.singleton.ItemContainer

class UpdateStock : AddToStock() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableFields()
        setUpFields()
    }

    private fun setUpFields() {
        viewModel.setProductType(ItemContainer.getProductType())
        binding.etProductOrServiceName.setText(ItemContainer.getName())
        binding.etProductQuantity.setText(ItemContainer.getQuantity())
        binding.etProductOrServiceUnitMeasure.setText(ItemContainer.getUnit())
        binding.etPrice.setText(ItemContainer.getPrice())
    }

    override fun saveProduct() {
        if (binding.etProductOrServiceName.text.toString() != "") {
            ItemContainer.setName(binding.etProductOrServiceName.text.toString())
        }
        if (binding.etProductOrServiceUnitMeasure.text.toString() != "") {
            ItemContainer.setUnit(binding.etProductOrServiceUnitMeasure.text.toString())
        }
        if (binding.etProductQuantity.text.toString() != "") {
            ItemContainer.setQuantity(binding.etProductQuantity.text.toString())
        }
        if (binding.etPrice.text.toString() != "") {
            ItemContainer.setPrice(binding.etPrice.text.toString())
        }

        val type: String = ItemContainer.getProductType()
        val name = ItemContainer.getName()
        val unit = ItemContainer.getUnit()
        val quantity: Int = Integer.parseInt(ItemContainer.getQuantity())
        val price: Double = java.lang.Double.parseDouble(ItemContainer.getPrice())

        val id: Int = arguments?.getString("KEY").toString().toInt()
        val product: Product = Product(id, type, name, unit, quantity, price)

        productDao.update(product)
        manager.openFragment(R.id.frameStock, ViewStock())

    }

    override fun setUpBinding() {
        super.setUpBinding()
        binding.btnSaveProductOrService.setOnClickListener() {
            this.saveProduct()
        }
        binding.btnBack.setOnClickListener() {
            manager.openFragment(R.id.frameStock, ViewStock())
        }
    }
}