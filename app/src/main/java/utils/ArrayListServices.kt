package utils

import java.util.ArrayList
import java.util.Collections
import java.util.HashSet

@Suppress("unused")
object ArrayListServices {
    fun sortAsc(arrayList: ArrayList<String>): ArrayList<String> {
        arrayList.sort()
        return arrayList
    }

    fun sortDesc(arrayList: ArrayList<String>): ArrayList<String> {
        Collections.sort(arrayList, Collections.reverseOrder())
        return arrayList
    }

    fun removeDuplicates(arrayList: ArrayList<String>): ArrayList<String> {
        val set = HashSet(arrayList)
        arrayList.clear()
        arrayList.addAll(set)
        return arrayList
    }
}
