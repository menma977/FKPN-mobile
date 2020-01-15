package com.mobile.fkpn.model

class MapConverter {
    fun convert(hashMap: HashMap<String, String>): String {
        return hashMap.toString().replace(", ", "&").replace("{", "").replace("}", "")
    }
}