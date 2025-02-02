package com.example.perpustakaan.ui.View.Kategori

import com.example.perpustakaan.ui.View.Penerbit.TambahBodyPenerbit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpustakaan.Navigasi.DestinasiNavigasi
import com.example.perpustakaan.ui.ViewModel.Kategori.UpdateKategoriViewModel
import com.example.perpustakaan.ui.ViewModel.Kategori.toKategori
import com.example.perpustakaan.ui.ViewModel.PenyediaViewModel
import com.example.perpustakaan.ui.Widget.CustomBottomAppBar
import com.example.perpustakaan.ui.Widget.CustomTopAppBar
import kotlinx.coroutines.launch


object DestinasiUpdateKategori : DestinasiNavigasi {
    override val route = "update_kategori"
    const val ID_Kategori = "id_kategori" // Key for the argument
    val routesWithArg = "$route/{$ID_Kategori}"// Argument placeholder in the route
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateKategoriView(
    navigateBack: () -> Unit,
    onProfilClick: () -> Unit,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit = {},
    onPenulisClick: () -> Unit,
    onPenerbitClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateKategoriViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    // Collect the UI state from the ViewModel
    val kategoriuiState = viewModel.kategoriuiState.value

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                judul = "Edit Kategori",
                onKategoriClick = {},
                onPenulisClick = onPenulisClick,
                onPenerbitClick = onPenerbitClick,
                scrollBehavior = scrollBehavior,
                onRefresh = {

                },
                isMenuEnabled = true, // Menampilkan ikon menu
                isKategoriEnabled = false, // Mengaktifkan menu Dosen
                isPenulisEnabled = true, // Menonaktifkan menu Mata Kuliah
                isPenerbitEnabled = true // Menonaktifkan menu Mata Kuliah
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                isHomeEnabled = false,
                onHomeClick = {},
                onProfileClick = onProfilClick,
                onAddDataClick = onAddClick, // Navigate to item entry when Add Data is clicked
                onBackClick = onBackClick// Handle Back click action
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Pass the UI state to the EntryBody
            TambahBodyKategori(
                insertKategoriUiState = kategoriuiState,
                errorMessage = viewModel.errorMessage,
                onKategoriValueChange = { updatedkValue ->
                    viewModel.updateKategoriState(updatedkValue) // Update ViewModel state
                },
                onSaveClick = {
                    kategoriuiState.insertKategoriUiEvent?.let { insertKategoriUiEvent ->
                        coroutineScope.launch {
                            // Call ViewModel update method
                            viewModel.updateKategori(
                                id_kategori = viewModel.id_kategori, // Pass the NIM from ViewModel
                                kategori = insertKategoriUiEvent.toKategori() // Convert InsertUiEvent to Mahasiswa

                            )
                            viewModel.ambilKategori()
                            if (viewModel.errorMessage.isEmpty()) {
                                navigateBack()
                            }
                        }
                    }
                }
            )
        }
    }
}
