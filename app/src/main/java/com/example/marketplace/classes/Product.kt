package com.example.marketplace.classes

data class Products(
    var ProductID:String="",
    var Name:String="",
    var Description:String="",
    var MRP:String="",
    var SP:String="",
    var ProductCategory:String="",
    var ProductBrand:String="",
    var CoverImage:String="",
    var Images:ArrayList<String> = ArrayList()
)