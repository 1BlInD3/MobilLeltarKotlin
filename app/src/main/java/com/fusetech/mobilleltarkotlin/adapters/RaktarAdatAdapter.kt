package com.fusetech.mobilleltarkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.dataItems.RaktarAdat

class RaktarAdatAdapter(val adatLista: ArrayList<RaktarAdat>, val listener: CurrentSelection): RecyclerView.Adapter<RaktarAdatAdapter.AdatViewHolder>() {
    inner class AdatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val cikks: TextView = itemView.findViewById(R.id.cikkszamHeader)
        val megnevezes1: TextView = itemView.findViewById(R.id.desc1Header)
        val megnevezes2: TextView = itemView.findViewById(R.id.desc2Header)
        val qty: TextView = itemView.findViewById(R.id.mennyisegHeader)
        val megjegyzes: TextView = itemView.findViewById(R.id.megjegyzesHeader)
        val biz: TextView = itemView.findViewById(R.id.bizszamTxt)
        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onCurrentClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdatViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return AdatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdatViewHolder, position: Int) {
        val currentItem = adatLista[position]
        holder.cikks.text = currentItem.cikkszam
        holder.megnevezes1.text = currentItem.megnevezes1
        holder.megnevezes2.text = currentItem.mennyiseg.toString().trim()
        holder.qty.text = currentItem.megnevezes2
        holder.megjegyzes.text = currentItem.megjegyzes
        holder.biz.text = currentItem.bizszam.toString().trim()
    }

    override fun getItemCount() = adatLista.size

    interface CurrentSelection{
        fun onCurrentClick(position: Int)
    }
}