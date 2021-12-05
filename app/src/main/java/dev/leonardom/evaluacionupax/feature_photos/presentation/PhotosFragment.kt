package dev.leonardom.evaluacionupax.feature_photos.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.leonardom.evaluacionupax.BuildConfig
import dev.leonardom.evaluacionupax.NavGraphDirections
import dev.leonardom.evaluacionupax.R
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.core.util.showSnackbar
import dev.leonardom.evaluacionupax.databinding.FragmentPhotosBinding
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class PhotosFragment: Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var layout: View

    private val viewModel: PhotosViewModel by viewModels()

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted) {
            capturePhotoFromCamera()
        }
    }

    private val requestGalleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted) {
            selectImageFromGallery()
        }
    }

    private var latestTmpUri: Uri? = null

    private val takeImageResult = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ isSuccess ->
        if(isSuccess) {
            latestTmpUri?.let { uri ->
                viewModel.updateTmpUri(uri)
            }
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){ uri ->
        uri?.let {
            viewModel.updateTmpUri(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        layout = binding.mainLayout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCamera.setOnClickListener {
            onClickRequestPermission(it, Manifest.permission.CAMERA)
        }

        binding.buttonGallery.setOnClickListener {
            onClickRequestPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.buttonSaveImage.setOnClickListener {
            viewModel.uploadImage(binding.imageViewPhotoPreview.drawable)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.cameraTmpUri.collect { uri ->
                binding.imageViewPhotoPreview.setImageURI(uri)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.displayProgressBar.collect { state ->
                binding.progressBar.isVisible = state == ProgressBarState.Loading
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.errorMessageQueue.collect { queue ->
                if(!queue.isEmpty()){
                    queue.peek()?.let { uiComponent ->
                        if(uiComponent is UIComponent.Dialog){
                            val action = NavGraphDirections.actionGlobalErrorDialog(
                                title = uiComponent.title,
                                description = uiComponent.description
                            )
                            findNavController().navigate(action)
                            viewModel.removeHeadMessage()
                        }
                    }
                }
            }
        }
    }

    private fun onClickRequestPermission(view: View, permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                when (permission) {
                    Manifest.permission.CAMERA -> {
                        // Acceso a la cámara
                        capturePhotoFromCamera()
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        // Acceso a la galería
                        selectImageFromGallery()
                    }
                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok_button),
                    anchorView = binding.buttonSaveImage
                ) {
                    when (permission) {
                        Manifest.permission.CAMERA -> {
                            requestCameraPermissionLauncher.launch(
                                permission
                            )
                        }
                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            requestGalleryPermissionLauncher.launch(
                                permission
                            )
                        }
                    }
                }
            }

            else -> {
                when (permission) {
                    Manifest.permission.CAMERA -> {
                        requestCameraPermissionLauncher.launch(
                            permission
                        )
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        requestGalleryPermissionLauncher.launch(
                            permission
                        )
                    }
                }
            }
        }
    }

    private fun capturePhotoFromCamera() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    /*
        Crear archivo temporal con la información de la última captura desde la cámara
        y generar URI a partir de este
     */
    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}