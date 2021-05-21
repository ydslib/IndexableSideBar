package com.yds.indexablesidebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.codemao.rokrok.login.widget.CityItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yds.indexable.SlideBar
import com.yds.indexablesidebar.bean.CountryBean
import com.yds.indexablesidebar.bean.CountryData
import com.yds.indexablesidebar.databinding.ActivityMainBinding
import com.yds.indexablesidebar.utils.AssetsUtil
import com.yds.indexablesidebar.widget.MyDividerDecoration

class MainActivity : AppCompatActivity() {
    lateinit var mDataBinding:ActivityMainBinding
    lateinit var dataList:MutableList<CountryBean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val fromAssets = AssetsUtil.getJson(this, "en_ADCountry.json")
        val type = object:TypeToken<CountryData>(){}.type
        val data= Gson().fromJson<CountryData>(fromAssets,type)
        dataList = data.data
        dataList.sortBy { it.countryName }
        val adapter = CountryAdapter(dataList)
        mDataBinding.recyclerView.adapter = adapter
        mDataBinding.recyclerView.addItemDecoration(MyDividerDecoration(this))
        mDataBinding.recyclerView.addItemDecoration(CityItemDecoration(this))
        setSlideBarData(dataList)
    }


    private fun setSlideBarData(data: MutableList<CountryBean>, select: Boolean = false) {
        var dataList = data.map { it.character }.distinct()
//        var dataList = arrayListOf<String>()
//        characterList.filter { it!="Current Location" && it!="Popular" }.forEach { dataList.add(it) }

        if (!select) {
            mDataBinding.slideView.setData(dataList)
        }
        mDataBinding.slideView.onIndexChangedListener = SlideBar.OnIndexChangedListener(
            onUp = { mDataBinding.tvCharacter.visibility = View.GONE },
            onIndexChanged = { letter ->
                mDataBinding.tvCharacter.visibility = View.VISIBLE
                mDataBinding.tvCharacter.text = letter
                val index = data.indexOfFirst { it.character == letter }
                (mDataBinding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(index, 0)
            }
        )
    }
}