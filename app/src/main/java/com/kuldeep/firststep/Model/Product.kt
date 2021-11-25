package com.kuldeep.firststep.Model

import java.util.HashMap

data class Product (var productId : String ?= null,
                    var productName : String?=null,
                    var brand : String ?= null,
                    var category : String ?= null,
                    var price : String ?= null,
                    var dateAdded : String ?= null,
                    var imageResource : String ?= null,
                    var description: String ?=null)