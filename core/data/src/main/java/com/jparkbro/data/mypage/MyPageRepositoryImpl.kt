package com.jparkbro.data.mypage

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import com.jparkbro.data.util.image.ImageCompressor
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.model.mypage.MyPageResponse
import com.jparkbro.model.mypage.MyReviewsRequest
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.model.mypage.ProfileImgRequest
import com.jparkbro.model.mypage.ProfileImgResponse
import com.jparkbro.model.mypage.UserContentResponse
import com.jparkbro.network.mypage.MyPageDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource,
    private val imageCompressor: ImageCompressor,
    @ApplicationContext private val context: Context,
) : MyPageRepository {

    override suspend fun getMyPageInfo(): Result<MyPageResponse> {
        return myPageDataSource.getMyPageInfo()
    }

    override suspend fun getUserContents(type: ContentType, lastId: Int?): Result<UserContentResponse> {
        return when (type) {
            ContentType.WATCHLIST -> myPageDataSource.getWatchList(
                type = "${ContentType.WATCHLIST.param}",
                lastId = lastId
            )
            ContentType.WATCHING -> myPageDataSource.getWatching(
                type = "${ContentType.WATCHING.param}",
                lastId = lastId
            )
            ContentType.FINISHED -> myPageDataSource.getFinished(
                type = "${ContentType.FINISHED.param}",
                lastId = lastId
            )
            ContentType.LIKED_ANIME -> myPageDataSource.getLikedAnimes(lastId = lastId)
            ContentType.LIKED_PERSON -> myPageDataSource.getLikedPersons(lastId = lastId)
        }
    }

    override suspend fun getMyReviews(request: MyReviewsRequest): Result<MyReviewsResponse> {
        return myPageDataSource.getMyReviews(request)
    }

    override suspend fun editProfileImg(contentUri: Uri): Result<ProfileImgResponse> {
        val mimeType = context.contentResolver.getType(contentUri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

        val compressedImage = imageCompressor.compressImage(
            contentUri = contentUri,
            compressionThreshold = 200 * 1024L,
        )

        return myPageDataSource.editProfileImg(
            request = ProfileImgRequest(
                imageData = compressedImage,
                mimeType = mimeType,
                filename = "profile_image.$extension"
            )
        )
    }
}