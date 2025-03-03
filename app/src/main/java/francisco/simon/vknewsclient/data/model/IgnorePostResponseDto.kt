package francisco.simon.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class IgnorePostResponseDto(
    @SerializedName("response")
    val ignorePost: IgnorePostDto
)
