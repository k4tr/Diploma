package com.example.kelineyt.data

sealed class Category(val category: String) {

    object Chair: Category("Краски и обои")
    object Cupboard: Category("Декоративные материалы")
    object Table: Category("Декоративные эффекты")
    object Accessory: Category("Инструменты")
    object Furniture: Category("Фасадная лепнина")
}