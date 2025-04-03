package com.sopt.clody.domain.util

object VersionComparator {
    fun compare(current: String, latest: String): Int {
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }

        for (i in 0 until maxOf(currentParts.size, latestParts.size)) {
            val c = currentParts.getOrNull(i) ?: 0
            val l = latestParts.getOrNull(i) ?: 0
            if (c < l) return -1
            if (c > l) return 1
        }
        return 0
    }
}
