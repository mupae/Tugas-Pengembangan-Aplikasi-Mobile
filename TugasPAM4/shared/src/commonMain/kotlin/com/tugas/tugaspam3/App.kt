package com.tugas.tugaspam3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.painterResource
import tugaspam3.shared.generated.resources.Res
import tugaspam3.shared.generated.resources.haimiya
import tugaspam3.shared.generated.resources.haimiyadark
import tugaspam3.shared.generated.resources.haimiyalight

data class ProfileUiState(
    val name: String = "Jalaludin Mufadhol Al Faruq",
    val bio: String = "Mahasiswa Teknik Informatika ITERA",
    val isDarkMode: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateProfile(newName: String, newBio: String) {
        _uiState.update { currentState ->
            currentState.copy(name = newName, bio = newBio)
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDarkMode = isDark)
        }
    }
}

@Composable
@Preview
fun App(viewModel: ProfileViewModel = viewModel { ProfileViewModel() }) {
    val uiState by viewModel.uiState.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    val overlayColor = if (uiState.isDarkMode) Color.Black.copy(alpha = 0.8f) else Color.LightGray.copy(alpha = 0.5f)
    val textColor = if (uiState.isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (uiState.isDarkMode) Color.LightGray else Color.DarkGray
    val cardColor = if (uiState.isDarkMode) Color(0xFF2C2C2C) else Color.White
    val backgrounds = if (uiState.isDarkMode) painterResource(Res.drawable.haimiyadark) else painterResource(Res.drawable.haimiyalight)
    val bColor = if (uiState.isDarkMode) Color.White else Color.Black
    val bTextColor = if (uiState.isDarkMode) Color.Black else Color.White

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()){
            Image(
                painter = backgrounds,
                contentDescription = "Background Profile",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(overlayColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) }
                    )
                }

                ProfileHeader(name = uiState.name, textColor = textColor)

                Spacer(modifier = Modifier.height(10.dp))

                if (isEditing) {
                    EditProfileSection(
                        initialName = uiState.name,
                        initialBio = uiState.bio,
                        cardColor = cardColor,
                        textColor = textColor,
                        onSave = { newName, newBio ->
                            // Update State di ViewModel
                            viewModel.updateProfile(newName, newBio)
                            isEditing = false // Tutup form
                        },
                        onCancel = { isEditing = false }
                    )
                } else {
                    ProfileCard(
                        bio = uiState.bio,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isEditing = true },
                        colors = ButtonDefaults.buttonColors(containerColor = bColor)
                    ) {
                        Text("Edit Profile", color = bTextColor)
                    }
                }
                    Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoItem(label = "Email", value = "jalaludin.124140154@student.itera.ac.id", textColor = textColor, secondaryTextColor = secondaryTextColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        InfoItem(label = "Phone", value = "+62 895-0899-7890", textColor = textColor, secondaryTextColor = secondaryTextColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        InfoItem(label = "Location", value = "Lampung Selatan, Lampung", textColor = textColor, secondaryTextColor = secondaryTextColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        LinkedItem(label = "Instagram", value = "mupae256", link = "https://www.instagram.com/mupae_256", textColor = textColor, secondaryTextColor = secondaryTextColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        LinkedItem(label = "TikTok", value = "mupae_256", link = "https://www.tiktok.com/@mupae_256", textColor = textColor, secondaryTextColor = secondaryTextColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        LinkedItem(label = "GitHub", value = "mupae", link = "https://github.com/mupae", textColor = textColor, secondaryTextColor = secondaryTextColor)
                    }
                }
            }
        }
    }
}

@Composable
fun EditProfileSection(
    initialName: String,
    initialBio: String,
    cardColor: Color,
    textColor: Color,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var nameInput by remember { mutableStateOf(initialName) }
    var bioInput by remember { mutableStateOf(initialBio) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                label = "Nama",
                value = nameInput,
                onValueChange = { nameInput = it },
                textColor = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            FormTextField(
                label = "Bio",
                value = bioInput,
                onValueChange = { bioInput = it },
                textColor = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = onCancel) {
                    Text("Batal", color = textColor)
                }
                Button(
                    onClick = { onSave(nameInput, bioInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Simpan", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    textColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedLabelColor = textColor,
            unfocusedLabelColor = Color.Gray
        )
    )
}

@Composable
fun ProfileHeader(name: String, textColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.haimiya),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileCard(bio: String, cardColor: Color, textColor: Color, secondaryTextColor: Color) {
    val link = LocalUriHandler.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tentang Saya",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = bio,
                fontSize = 14.sp,
                color = secondaryTextColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, textColor: Color, secondaryTextColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = secondaryTextColor,
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = textColor
        )
    }
}

@Composable
fun LinkedItem(label: String, value: String, link: String, textColor: Color, secondaryTextColor: Color){
    val handler = LocalUriHandler.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = secondaryTextColor,
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = textColor,
            modifier = Modifier.clickable {
                handler.openUri(link)
            }
        )
    }
}
