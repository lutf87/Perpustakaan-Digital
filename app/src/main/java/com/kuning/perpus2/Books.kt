package com.kuning.perpus2

class Books {
    var judul: String? = null
    var author: String ? = null
    var tahun: String? = null
    var harga: String? = null
    var key: String? = null


    constructor(){}

    constructor(judul: String?, author: String?, tahun: String?, harga: String) {
        this.judul = judul
        this.author = author
        this.tahun = tahun
        this.harga = harga
    }
}