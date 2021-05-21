package com.yds.indexablesidebar.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class AssetsUtil {
    companion object {
        fun getFromAssets(context: Context, fileName: String): String {
            var result = ""
            try {
                val input = context.resources.assets.open(fileName)
                var ch = input.read()
                val out = ByteArrayOutputStream()
                while (ch != -1) {
                    out.write(ch)
                    ch = input.read()
                }
                val buff = out.toByteArray()
                out.close()
                input.close()
                result = String.format("UTF-8",buff)
            } catch (e: Exception) {
                e.printStackTrace();
            }
            return result
        }

        //读取方法
        fun getJson(context: Context, fileName: String?): String? {
            val stringBuilder = StringBuilder()
            try {
                val assetManager = context.assets
                val bf = BufferedReader(
                    InputStreamReader(
                        assetManager.open(
                            fileName!!
                        )
                    )
                )
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return stringBuilder.toString()
        }
    }
}