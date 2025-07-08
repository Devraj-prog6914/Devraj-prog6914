package com.example.mayapp

object dataobj {
    private lateinit var datalist: ArrayList<datamodel>

    fun getdata():ArrayList<datamodel> {
        datalist = arrayListOf(
            datamodel(R.drawable.uuk, "Skoda", "Tamil nadu"),
            datamodel(R.drawable.uuk, "Mahindra", "Tamil nadu"),
            datamodel(R.drawable.p, "Hyundai", "Maharashtra"),
            datamodel(R.drawable.uuk, "Tata", "Delhi"),
            datamodel(R.drawable.uuk, "Mahindra", "Maharashtra"),
            datamodel(R.drawable.p, "Maruti suzuki", "Tamil nadu"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Kia", "Pune"),
            datamodel(R.drawable.p, "Hundia", "Maharashtra"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Mahindra", "Telangana"),
            datamodel(R.drawable.p, "Skoda", "Andhra pradesh"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Mahindra", "Maharashtra"),
            datamodel(R.drawable.p, "Tata", "Maharashtra"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Renault", "Bihar"),
            datamodel(R.drawable.p, "Hundia", "Maharashtra"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Toyota", "Karnataka"),
            datamodel(R.drawable.p, "Hundia", "Maharashtra"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Kia", "Karnataka"),
            datamodel(R.drawable.p, "Hundia", "Uttar pradesh"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Hyundai", "Bihar"),
            datamodel(R.drawable.p, "Toyota", "Maharashtra"), datamodel(R.drawable.uuk, "Tesla", "Maharashtra"),
            datamodel(R.drawable.uuk, "Mahindra", "Maharashtra"),
            datamodel(R.drawable.p, "Toyota", "Gujrat"),



        )

        return datalist
    }
}