package com.example.hideandseek.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.net.URL
import javax.inject.Inject

interface MapRepository {
    suspend fun fetchMap(latestUser: UserData, allLocation: List<LocationData>, allTraps: List<TrapData>): Bitmap
}

class MapRepositoryImpl @Inject constructor(
    private val coroutineScope: CoroutineScope,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : MapRepository {

    override suspend fun fetchMap(latestUser: UserData, allLocation: List<LocationData>, allTraps: List<TrapData>): Bitmap {
        Log.d("MapRepository", "fetchMapが呼ばれました")
        val url: String = makeUrl(latestUser, allLocation, allTraps)
        val originalDeferred = coroutineScope.async(ioDispatcher) {
            getOriginalBitmap(url)
        }
        return originalDeferred.await()
    }

    private fun getOriginalBitmap(url: String): Bitmap =
        URL(url).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun makeUrl(latestUser: UserData, allLocation: List<LocationData>, allTraps: List<TrapData>): String {
        // 自分の位置情報のurl
        val iconUrlHide = "https://lh3.googleusercontent.com/fTB0kRIlKDrQjTC1tiQC4VNIxcfRCKkuWLQec-sqIvtDrZhQ_kLS5PC5MILszDwMED_-kHW4vuuRQM71SLQnAqHnkboIeiFA69ws5xzuEMmOdy3NcsIFxSpIIBYYe7-9MkXX9Zpful9GfZ-_Ufvda5hrEMjTRKOdhg9fHtlRfdA5-SxXeUcl_bw1xOPj5Jg7pI-A0f1soN1G2wsbXmWlIk2F8zQ6_6_zaQ0WQPkaXShsnRXZJNPB078BZNOpzETx1KdvwCbH2uqnHPLLkiQBCvAnSbt-FHc_Y7UTZZ8KHCG6KIdnnwxfQlZn4joJaO5dJpC9kQMZoUn1cs_sJ-PF5h5PDOA26s-CKwgxifSaqVlmctqPupXmXoXfduLNaJm_z9CILnWAqCV6U5NOf8NZV0waqPQmhenk_8ANc2fNtgltz4--JxolPTz27PhRrXk4rADQEVLEJ1AWfpmWF93R94rkeDhKo39RCpndm52cWA0oqC6x_aWQj6F9KTukrb_nYyNZqUt14nWYUc8dzU22GYirIJcQBDHG4yLy8pv7DUuzd140emlx4SMBv4p919ubZi6qUc9gNPzRPql0BixKgGrwLg0SXCwyKUhM6a5WS-vBBAyCTSJCybfQ2QfsMGmCNuB437B8G1e9i581VBGymHLPBxq0vHPTrilYrag3EZWhjrLnIShW4h7coRf6DUdgZb-UyZsrQzhtV27R_dtXjfiNFggmRsDLh5jJgLQ1GHb_qZBistDosQ8da7wsLi0pQiKwfr68G_MTncpARwGREhjLeAuh1vPC2JFm7Oc7KIUtGOgCErB6u44Skbf-_w9tapn1uyFRqUlnMk0Rqww6sT5_dndLgXMo17KI893xKbA3VDXMmO4NtfCOH4mjdMPVBs8rtoE9yVnjRi0JhLoFAymituMyycwFC3aQq-Zejg6c16EkxUGS9oB0nKbeEcA-AqET8eaVJeKKx_hxmHqEagXA1a2bT6TokgE6WRyBUXjqu1eZaksGww6gG4UfaoSX9vYIGWG5PrugqMlzMV-XE27ddRh9Be6kf3X7ToEe7nF7CS8p7vV-BfaX3ms=s32-no?authuser=0"

        var url = "https://maps.googleapis.com/maps/api/staticmap" +
            "?center=${latestUser.latitude},${latestUser.longitude}" +
            "&size=640x640&scale=1" +
            "&zoom=18" +
            "&key=AIzaSyA-cfLegBoleKaT2TbU5R4K1uRkzBR6vUQ" +
            "&markers=icon:" + iconUrlHide + "|${latestUser.latitude},${latestUser.longitude}"

        // 他人の位置を追加
        if (allLocation.isNotEmpty()) {
            // ユーザーの位置情報
            for (i in allLocation.indices) {
                if (allLocation[i].status != 1 && allLocation[i].status != -1) {
                    url += "&markers=icon:" + iconUrlHide + "|${allLocation[i].latitude},${allLocation[i].longitude}"
                }
            }
        }

        // trapの位置情報
        if (allTraps.isNotEmpty()) {
            for (i in allTraps.indices) {
                if (allTraps[i].status == 0) {
                    val iconUrlTrap = "https://lh3.googleusercontent.com/6B1VLkjCpGk8BtAZfVfpSHxLlfHi1UI0Pry1DdgCi7PPrNr1846g818b2Qq1SjT6_-ntECnAFgQEsmEDT7x4LDC5Tgxhk9P9KwoyAFWnI2G6J2FIFRNfjDgMZe83SXZr4FK0lzsOO4VpcQD-bWRiLJ9NFx_Xk4qR68NihN7ZU9zm8c4Kopy7KrsZZLTOlasbLauicoLB3Et98RDiI23q79h6i1Ui-l8mqieUH29mTfOistvaLbVx8S0RuNW1qKlm_LJdW8btMli0SN9BUao_IwuHXf0CbPv2gCxJDK7pz4RoKEz3WYLnEZG-tC12UQrWIE8b4YA7EwHqOCXiePmRITZqAU-4_8HjNrN-ZZJd12nov_FjMWRLtaDk6lU3ZMctlBlSz-_0ep63SZJ9Gbo59mwV1VhM7ZrWj648fMX_l_15W4Si7W7t1BdcjkIOKAL1ORdQ1A-51OgVNjdBoucUip7U2I20vJQ1SlWjfNs2bb0fxO3ue8kEMko1RlbVgYyYBTWf2CkUJXPA05MSCSRYZ8-7goG01K2dsL-az12K664_zQF0_E5X2zR5X7BqiYC_bV2uYIVYyYn4geLDa-cxVmPFVA6AjyTfG8SpEoPoZwRTZ648hdkbYi7dnaYHEzDNyTxdCnzfi620RfQKMuLsKF5vhIIaSfHhmWPpy_ADHNezNH2oHHWgYmT-kuJ2HIR99MHvOlEsU30NGh-bNfkfWqmdoGFivR8IoQqzaaA-wZu23SyaFsXZ9Z4WTRWqhXRwrrGNqCByDiZgqiFUDpnvsaQ-dqe33HGduap7vYnbikhyMsBql-gShf53hr78-bTouQeVweWXc5xNPBXRl4FXsxyt-L7VlJPSwSu6eJzuQBzrAjLGLZdmwF-U03Sx1k-dl5_X2EE0b3CXkvywms9v0sCb9m4gDbIUQXZ9g3stn31ftdsF4SHaAgmoLIo7PCDosnRwf5AkcrkK-VSiByb8dyjVCzvZnfkWLN5eMDPHO2P36nLWMO81lA2xwUlYbTtodPA3E9IDd2oDQQLZJJxMsURqZTltaSDJK4dmcj-RGmzscfXISTTRmV8VIsY=w24-h33-no?authuser=0"
                    url += "&markers=icon:$iconUrlTrap|${allTraps[i].latitude},${allTraps[i].longitude}"
                }
            }
        }

        return url
    }
}
