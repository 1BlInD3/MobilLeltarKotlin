package com.fusetech.mobilleltarkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.dataItems.CikkItems


class CikkItemAdapter(private val cikkList: ArrayList<CikkItems>): RecyclerView.Adapter<CikkItemAdapter.CikkItemViewHolder>() {
    class CikkItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mennyisegText: TextView = itemView.findViewById(R.id.cikkszamHeader)
        val polcText : TextView = itemView.findViewById(R.id.polcText)
        val raktarText: TextView = itemView.findViewById(R.id.desc1Header)
        val allapotText: TextView = itemView.findViewById(R.id.megjegyzesHeader)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CikkItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cikk_view,parent,false)
        return CikkItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CikkItemViewHolder, position: Int) {
        val currentItem = cikkList[position]
        holder.mennyisegText.text = currentItem.mennyiseg.toString()
        holder.polcText.text = currentItem.polc
        holder.raktarText.text = currentItem.raktar
        holder.allapotText.text = currentItem.allapot
    }

    override fun getItemCount() = cikkList.size
}