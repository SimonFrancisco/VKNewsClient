package francisco.simon.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class IgnorePostDto(
    @SerializedName("status")
    val status: Boolean
)
