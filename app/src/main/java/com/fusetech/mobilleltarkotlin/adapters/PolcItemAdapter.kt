package com.fusetech.mobilleltarkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.dataItems.PolcItems

class PolcItemAdapter(private val polcItems: ArrayList<PolcItems>): RecyclerView.Adapter<PolcItemAdapter.PolcItemViewHolder>() {
    class PolcItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mennyisegText : TextView = itemView.findViewById(R.id.cikkszamHeader)
        val unitText: TextView = itemView.findViewById(R.id.polcText)
        val megnevezes1Text : TextView = itemView.findViewById(R.id.desc1Header)
        val megnevezes2Text: TextView = itemView.findViewById(R.id.megnevezes2)
        val intrem: TextView =  itemView.findViewById(R.id.mennyisegHeader)
        val allapotText: TextView = itemView.findViewById(R.id.megjegyzesHeader)
        val cikkszamText: TextView = itemView.findViewById(R.id.cikkszam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolcItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.polc_view,parent,false)
        return PolcItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PolcItemViewHolder, position: Int) {
        val currentItem = polcItems[position]
        holder.mennyisegText.text = currentItem.mennyiseg.toString()
        holder.unitText.text = currentItem.egyseg
        holder.megnevezes1Text.text = currentItem.megnevezes1
        holder.megnevezes2Text.text = currentItem.megnevezes2
        holder.intrem.text = currentItem.intrem
        holder.allapotText.text = currentItem.allapot
        holder.cikkszamText.text = currentItem.cikk
    }

    override fun getItemCount() = polcItems.size
}