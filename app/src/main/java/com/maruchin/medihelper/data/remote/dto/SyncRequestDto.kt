package com.maruchin.medihelper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SyncRequestDto<T>(
    @SerializedName(value = "insertUpdateDtoList")
    val insertUpdateDtoList: List<T>,

    @SerializedName(value = "deleteRemoteIdList")
    val deleteRemoteIdList: List<Long>
)