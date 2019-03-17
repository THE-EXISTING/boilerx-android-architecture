package com.existing.boilerx.app.repository.network.model.reponse

import com.existing.nextwork.engine.model.NextworkResponseResult
import com.google.gson.annotations.SerializedName

/**
 * Created by Nonthawit on 3/13/2016.
 */
class PhotoListResponse(
        @SerializedName("success")
        var success: Boolean = false)
    : NextworkResponseResult<List<PhotoResponse>>()

