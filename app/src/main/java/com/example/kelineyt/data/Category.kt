package com.example.kelineyt.data

sealed class Category(val category: String) {

    object Chair: Category("Colors")
    object Cupboard: Category("Декоративные эффекты")
    object Table: Category("Декоративные материалы")
    object Accessory: Category("Инструменты")
    object Furniture: Category("Фасадная лепнина")
}