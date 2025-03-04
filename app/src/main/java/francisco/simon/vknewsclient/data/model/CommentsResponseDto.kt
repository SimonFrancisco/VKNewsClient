package francisco.simon.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response")
    val commentsResponse:CommentsContentDto
)
