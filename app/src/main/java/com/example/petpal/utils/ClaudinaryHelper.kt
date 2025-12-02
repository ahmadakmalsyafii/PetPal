package com.example.petpal.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryHelper {
    private const val CLOUD_NAME = "dzvmb5wru"
    private const val UPLOAD_PRESET = "petpal_preset"

    fun init(context: Context) {
        val config = HashMap<String, String>()
        config["cloud_name"] = CLOUD_NAME
        try {
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // MediaManager sudah terinisialisasi, abaikan
        }
    }

    // Fungsi suspend untuk upload gambar agar bisa dipanggil di ViewModel
    suspend fun uploadImage(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .unsigned(UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    // Ambil URL aman (https)
                    val secureUrl = resultData["secure_url"] as? String ?: ""
                    continuation.resume(secureUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(Exception("Upload Error: ${error.description}"))
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }
}