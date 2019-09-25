package com.example.medihelper.remotedatabase.pojos

import com.google.gson.annotations.SerializedName

data class SyncRequestDto<T>(
    @SerializedName(value = "deleteRemoteIdList")
    val deleteRemoteIdList: List<Long>,

    @SerializedName(value = "insertUpdateDtoList")
    val insertUpdateDtoList: List<T>
)