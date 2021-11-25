package com.kuldeep.firststep.Model

import java.util.ArrayList

data class Category (var categoryName : String?=null,
                     var productList : ArrayList<Product> ?= null)