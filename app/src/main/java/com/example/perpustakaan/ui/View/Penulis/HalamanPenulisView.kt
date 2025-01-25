package com.example.perpustakaan.ui.View.Penulis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpustakaan.Navigasi.DestinasiNavigasi
import com.example.perpustakaan.R
import com.example.perpustakaan.model.Penulis
import com.example.perpustakaan.ui.ViewModel.Penulis.HomePenulisUiState
import com.example.perpustakaan.ui.ViewModel.Penulis.HomePenulisViewModel
import com.example.perpustakaan.ui.ViewModel.PenyediaViewModel
import com.example.perpustakaan.ui.Widget.CustomBottomAppBar
import com.example.perpustakaan.ui.Widget.CustomTopAppBar

object DestinasiHomePenulis : DestinasiNavigasi {
   override val route = "home Penulis"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePenulis(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onProfilClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    onPenerbitClick: () -> Unit,
    onKategoriClick: () -> Unit,
    onDetailClick: (Int) -> Unit = {},
    onUpdateClick: (Penulis) -> Unit = {}, // Menambahkan parameter untuk update
    viewModel: HomePenulisViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                judul = "Penulis",
                onKategoriClick = onKategoriClick,
                onPenulisClick = {},
                onPenerbitClick = onPenerbitClick,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                viewModel.getPenulis()
                },
                isMenuEnabled = true, // Menampilkan ikon menu
                isKategoriEnabled = true, // Mengaktifkan menu Dosen
                isPenulisEnabled = false, // Menonaktifkan menu Mata Kuliah
                isPenerbitEnabled = true // Menonaktifkan menu Mata Kuliah
            )
        },

        bottomBar = {
            CustomBottomAppBar(
                isBackEnabled =false,
                onHomeClick = onHomeClick,
                onProfileClick = onProfilClick,
                onAddDataClick = navigateToItemEntry, // Navigate to item entry when Add Data is clicked
                onBackClick = { } // Handle Back click action
            )
        },
    ) { innerPadding ->
        HomeStatus(
            homePenulisUiState = viewModel.penulisUiState,
            retryAction = { viewModel.getPenulis() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.deletePenulis(it.id_penulis)
                viewModel.getPenulis()
            },
            onUpdateClick = onUpdateClick// Menggunakan fungsi update

        )
    }
}

@Composable
fun HomeStatus(
    homePenulisUiState: HomePenulisUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Penulis) -> Unit = {},
    onUpdateClick: (Penulis) -> Unit = {}, // Menambahkan parameter untuk update
    onDetailClick: (Int) -> Unit
) {
    when (homePenulisUiState) {
        is HomePenulisUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomePenulisUiState.Success ->
            if (homePenulisUiState.penulis.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data penulis", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                BukuList(
                    penulis = homePenulisUiState.penulis,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.id_penulis)
                    },
                    onDeleteClick = {
                        onDeleteClick(it)
                    },
                    onUpdateClick = onUpdateClick // Menggunakan fungsi update
                )
            }
        is HomePenulisUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.loadingg),
            contentDescription = stringResource(R.string.loading)
        )
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = stringResource(R.string.loading_failed),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun BukuList(
    penulis: List<Penulis>,
    modifier: Modifier = Modifier,
    onDetailClick: (Penulis) -> Unit,
    onDeleteClick: (Penulis) -> Unit = {},
    onUpdateClick: (Penulis) -> Unit = {}  // Menambahkan parameter untuk update
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(penulis) { penulis ->
            BukuCard(
                penulis = penulis,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(penulis) },
                onDeleteClick = {
                    onDeleteClick(penulis)
                },
                onUpdateClick = {
                    onUpdateClick(penulis)  // Menambahkan fungsi update
                }
            )
        }
    }
}

@Composable
fun BukuCard(
    penulis: Penulis,
    modifier: Modifier = Modifier,
    onDeleteClick: (Penulis) -> Unit = {},
    onUpdateClick: (Penulis) -> Unit = {},
    deleteIconColor: Color = Color.Black, // Warna ikon delete (putih)
    updateIconColor: Color = Color.Blue, // Warna ikon update (putih)
) {
    // Card dengan gradasi dan efek bayangan
    Card(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF1F1F1), Color(0xFFE0E0E0)), // Gradasi warna abu-abu terang ke gelap
                    start = Offset(0f, 0f),
                    end = Offset(0f, 1000f)
                )
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)

    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = penulis.id_penulis.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,

                        )

                )

                Spacer(Modifier.weight(1f))

                // Tombol Hapus dengan efek timbul
                IconButton(onClick = { onDeleteClick(penulis) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = deleteIconColor,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(60.dp)
                            .graphicsLayer {
                                // Menambahkan efek timbul untuk ikon
                                shadowElevation = 4.dp.toPx() // Elevasi bayangan
                                shape = CircleShape
                                clip = true
                            }
                    )
                }

                // Tombol Update dengan efek timbul
                IconButton(onClick = { onUpdateClick(penulis) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Update",
                        tint = updateIconColor,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(60.dp)
                            .graphicsLayer {
                                // Menambahkan efek timbul untuk ikon
                                shadowElevation = 4.dp.toPx()
                                shape = CircleShape
                                clip = true
                            }
                    )
                }
            }

            // Nama Penulis dengan efek timbul
            Text(
                text = penulis.nama_penulis,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    )
            )

            // Biografi penulis dengan sedikit transparansi dan efek timbul
            Text(
                text = penulis.biografi,
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )

        }
    }
}
