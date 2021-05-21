package com.yds.indexablesidebar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.promeg.pinyinhelper.Pinyin
import com.yds.indexablesidebar.bean.CountryBean

class CountryAdapter(var data:MutableList<CountryBean>):RecyclerView.Adapter<CountryAdapter.MyViewHolder>() {

    init {
        data.forEach {
            it.character = Pinyin.toPinyin(it.countryName[0])[0].toString()
        }
    }

    var onItemClickListener: ((Int, CountryBean) -> Unit)? = null

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvCityName = itemView.findViewById<TextView>(R.id.tvCityName)
        val tvCountryCode = itemView.findViewById<TextView>(R.id.tvCountryCode)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvCityName.text = data[position].countryName
        if (data[position].phoneCode != "(null)") {
            holder.tvCountryCode.text = data[position].phoneCode
        }else{
            holder.tvCountryCode.text = ""
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position, data[position])
        }
    }

    override fun getItemCount() = data.size
}