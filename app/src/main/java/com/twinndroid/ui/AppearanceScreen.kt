package com.twinndroid.ui

import com.twinndroid.R
import androidx.compose.ui.res.stringResource
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.twinndroid.ui.theme.ThemeRepository

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AppearanceScreen(vm: MainViewModel) {
    val context = LocalContext.current
    val repo = remember { ThemeRepository.get(context) }
    val cfg by repo.config.collectAsState()

    var currentIcon by remember { mutableStateOf(AppIcons.current(context)) }
    var pendingIcon by remember { mutableStateOf<AppIcon?>(null) }
    var customUri by remember { mutableStateOf<Uri?>(null) }
    var customPreview by remember { mutableStateOf<ImageBitmap?>(null) }
    var shortcutLabel by remember { mutableStateOf("JotiDroid") }
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            customUri = uri
            customPreview = runCatching { AppIcons.loadAdaptive(context, uri)?.asImageBitmap() }.getOrNull()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, scrolledContainerColor = Color.Transparent),
                title = { Text(stringResource(R.string.appearance)) },
                navigationIcon = { IconButton(onClick = { vm.screen = Screen.Home }) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back)) } },
            )
        },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Panel("Tamaño del texto") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("A", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = cfg.fontScale,
                        onValueChange = { v -> repo.update { it.copy(fontScale = (v * 20).toInt() / 20f) } },
                        valueRange = 0.85f..1.4f,
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                    )
                    Text("A", style = MaterialTheme.typography.titleLarge)
                }
                Hint("Escala actual: ${(cfg.fontScale * 100).toInt()}%")
            }

            Panel("Icono de la aplicación") {
                Hint("Elige el fondo del icono (blanco o negro) para JotiDroid.")
                Spacer(Modifier.height(12.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppIcons.all.forEach { icon ->
                        val selected = icon === currentIcon
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(64.dp)) {
                            Box(
                                Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Brush.linearGradient(listOf(icon.bgStart, icon.bgEnd)))
                                    .border(
                                        if (selected) BorderStroke(3.dp, MaterialTheme.colorScheme.primary) else BorderStroke(0.dp, Color.Transparent),
                                        RoundedCornerShape(18.dp),
                                    )
                                    .clickable { if (!selected) pendingIcon = icon },
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(painterResource(icon.foreground), null, Modifier.size(60.dp))
                                if (selected) {
                                    Icon(
                                        Icons.Rounded.Check, null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(16.dp)
                                            .clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                                    )
                                }
                            }
                            Text(icon.name, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Text("Imagen propia (Acceso directo)", style = MaterialTheme.typography.labelLarge)
                Hint("Añade un acceso directo con una imagen personalizada en tu pantalla de inicio.")
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(60.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .clickable { pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        contentAlignment = Alignment.Center,
                    ) {
                        val img = customPreview
                        if (img != null) Image(img, null, Modifier.size(60.dp))
                        else Icon(Icons.Rounded.AddPhotoAlternate, "Elegir imagen")
                    }
                    Spacer(Modifier.width(12.dp))
                    OutlinedTextField(
                        value = shortcutLabel,
                        onValueChange = { shortcutLabel = it.take(24) },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(12.dp))
                ButtonRow {
                    OutlinedButton(onClick = { pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                        Text(if (customUri == null) "Elegir imagen" else "Cambiar imagen")
                    }
                    Button(
                        enabled = customUri != null,
                        onClick = {
                            val ok = runCatching { AppIcons.pinCustomShortcut(context, customUri!!, shortcutLabel) }.getOrDefault(false)
                            if (!ok) vm.message = "Acceso directo no soportado"
                        },
                    ) { Text("Añadir al inicio") }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    pendingIcon?.let { icon ->
        AlertDialog(
            onDismissRequest = { pendingIcon = null },
            title = { Text("Cambiar icono a ${icon.name}") },
            text = { Text("El nuevo icono se aplicará al salir de JotiDroid.") },
            confirmButton = {
                TextButton(onClick = {
                    AppIcons.choose(context, icon)
                    currentIcon = icon
                    pendingIcon = null
                }) { Text("Cambiar") }
            },
            dismissButton = { TextButton(onClick = { pendingIcon = null }) { Text("Cancelar") } },
        )
    }
}

@Composable
internal fun Panel(title: String, content: @Composable () -> Unit) {
    TwinnCard {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
internal fun Hint(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}
