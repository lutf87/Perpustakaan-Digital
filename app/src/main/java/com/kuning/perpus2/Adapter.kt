package com.kuning.perpus2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Adapter(private var list_book: ArrayList<Books>, context: Context):
RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var context: Context
    private var db: DatabaseReference? = null

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val Judul: TextView
        val Author: TextView
        val Tahun: TextView
        val Harga: TextView
        val ListItem: LinearLayout
        val ButtonUp: Button
        val ButtonDel: Button

        init {
            Judul = itemView.findViewById(R.id.judul)
            Author = itemView.findViewById(R.id.author)
            Tahun = itemView.findViewById(R.id.tahun)
            Harga = itemView.findViewById(R.id.harga)
            ButtonUp = itemView.findViewById(R.id.btn_update)
            ButtonDel = itemView.findViewById(R.id.btn_delete)
            ListItem = itemView.findViewById(R.id.list_item)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v:View = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_buku,
        parent,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        db = FirebaseDatabase.getInstance().reference

        val Judul: String? = list_book.get(position).judul
        val Author: String? = list_book.get(position).author
        val Tahun: String? = list_book.get(position).tahun
        val Harga: String? = list_book.get(position).harga

        holder.Judul.text = "$Judul"
        holder.Author.text = "$Author"
        holder.Tahun.text = "$Tahun"
        holder.Harga.text = "$Harga"
        holder.ButtonUp.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putString("judulBuku", list_book[position].judul)
            bundle.putString("authorBuku", list_book[position].author)
            bundle.putString("tahunBuku", list_book[position].tahun)
            bundle.putString("hargaBuku", list_book[position].harga)
            bundle.putString("getPrimaryKey", list_book[position].key)
            val intent = Intent(view.context, UpdateBuku::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
        holder.ButtonDel.setOnClickListener { view ->

            listener?.onDeleteData(list_book.get(position), position)

        }
    }

    override fun getItemCount(): Int {
        return list_book.size
    }

    var listener: dataListener? = null

    init {
            this.context = context
        this.listener = context as MainActivity

    }

    interface dataListener: View.OnClickListener {
        fun onDeleteData(data: Books?, position: Int)
    }
    @SuppressLint("NotConstructor")
    fun Adapter(list_book: ArrayList<Books>?, context: Context?) {
        this.list_book = list_book!!
        this.context = context!!
        listener = context as MainActivity?
    }
}