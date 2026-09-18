package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.CustomDhikrEntity
import com.example.model.DhikrItem
import com.example.model.DhikrPresets

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DhikrSelectionSheet(
    activeDhikr: DhikrItem,
    currentTarget: Int,
    isUnlimited: Boolean,
    is33x3Mode: Boolean,
    customDhikrList: List<CustomDhikrEntity>,
    onSelectDhikr: (DhikrItem) -> Unit,
    onSelectTarget: (Int) -> Unit,
    onToggleUnlimited: (Boolean) -> Unit,
    onToggle33x3Mode: (Boolean) -> Unit,
    onCreateCustomDhikr: (String, String, Int) -> Unit,
    onDeleteCustomDhikr: (CustomDhikrEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCustomDialog by remember { mutableStateOf(false) }
    var showCustomTargetDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All" to stringResource(R.string.category_all),
        "Essential" to stringResource(R.string.category_essential),
        "Forgiveness" to stringResource(R.string.category_forgiveness),
        "Salawat" to stringResource(R.string.category_salawat),
        "Supplication" to stringResource(R.string.category_supplication),
        "Praise" to stringResource(R.string.category_praise)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("dhikr_selection_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.dhikr_mode_selection),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(14.dp))

                // 33x3 Mode Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (is33x3Mode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.mode_33x3_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(R.string.mode_33x3_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = is33x3Mode,
                            onCheckedChange = {
                                onToggle33x3Mode(it)
                                onDismiss()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Targets row (only applicable when not in 33x3 mode)
                if (!is33x3Mode) {
                    Text(
                        text = stringResource(R.string.target_count),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DhikrPresets.TARGET_OPTIONS.forEach { targetOption ->
                            val isSelected = !isUnlimited && currentTarget == targetOption
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onSelectTarget(targetOption)
                                },
                                label = { Text("$targetOption") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }

                        // Custom target option
                        val isCustomTarget = !isUnlimited && !DhikrPresets.TARGET_OPTIONS.contains(currentTarget)
                        FilterChip(
                            selected = isCustomTarget,
                            onClick = { showCustomTargetDialog = true },
                            label = { Text(if (isCustomTarget) stringResource(R.string.target_prefix, currentTarget) else stringResource(R.string.custom_target_chip)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )

                        // Unlimited mode
                        FilterChip(
                            selected = isUnlimited,
                            onClick = {
                                onToggleUnlimited(!isUnlimited)
                            },
                            label = { Text(stringResource(R.string.unlimited_symbol)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Search & Filter Header
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.search_dhikr_placeholder), fontSize = 13.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category chips scroll
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { (catKey, catLabel) ->
                        FilterChip(
                            selected = selectedCategory == catKey,
                            onClick = { selectedCategory = catKey },
                            label = { Text(catLabel, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Presets section title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.builtin_dhikr_presets),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedButton(
                        onClick = { showCustomDialog = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.add_custom), style = MaterialTheme.typography.labelMedium)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Filtered built-in presets
            val filteredPresets = DhikrPresets.PRESETS.filter { dhikr ->
                val matchesCategory = selectedCategory == "All" || dhikr.category.equals(selectedCategory, ignoreCase = true)
                val localizedName = dhikr.getLocalizedName(context)
                val localizedMeaning = dhikr.getLocalizedMeaning(context)
                val matchesSearch = searchQuery.isBlank() ||
                        dhikr.name.contains(searchQuery, ignoreCase = true) ||
                        dhikr.arabic.contains(searchQuery, ignoreCase = true) ||
                        localizedName.contains(searchQuery, ignoreCase = true) ||
                        localizedMeaning.contains(searchQuery, ignoreCase = true)
                matchesCategory && matchesSearch
            }

            items(filteredPresets) { dhikr ->
                val isSelected = !is33x3Mode && activeDhikr.id == dhikr.id
                DhikrListItem(
                    name = dhikr.getLocalizedName(context),
                    arabic = dhikr.arabic,
                    meaning = dhikr.getLocalizedMeaning(context),
                    target = dhikr.defaultTarget,
                    isSelected = isSelected,
                    onSelect = {
                        onSelectDhikr(dhikr)
                        onDismiss()
                    }
                )
            }

            // Custom Dhikr section
            if (customDhikrList.isNotEmpty() && (selectedCategory == "All")) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.custom_dhikr),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                val filteredCustom = customDhikrList.filter { customItem ->
                    searchQuery.isBlank() ||
                            customItem.name.contains(searchQuery, ignoreCase = true) ||
                            customItem.arabic.contains(searchQuery, ignoreCase = true)
                }

                items(filteredCustom) { customItem ->
                    val isSelected = !is33x3Mode && activeDhikr.id == "custom_${customItem.id}"
                    val customLabel = stringResource(R.string.custom_dhikr)
                    DhikrListItem(
                        name = customItem.name,
                        arabic = customItem.arabic,
                        meaning = customLabel,
                        target = customItem.target,
                        isSelected = isSelected,
                        onSelect = {
                            val dhikr = DhikrItem(
                                id = "custom_${customItem.id}",
                                name = customItem.name,
                                arabic = customItem.arabic,
                                meaning = customLabel,
                                defaultTarget = customItem.target,
                                isCustom = true
                            )
                            onSelectDhikr(dhikr)
                            onDismiss()
                        },
                        onDelete = {
                            onDeleteCustomDhikr(customItem)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }

    // Dialog for adding custom Dhikr
    if (showCustomDialog) {
        var customName by remember { mutableStateOf("") }
        var customArabic by remember { mutableStateOf("") }
        var customTargetText by remember { mutableStateOf("100") }

        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text(stringResource(R.string.create_custom_dhikr)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = customArabic,
                        onValueChange = { customArabic = it },
                        label = { Text(stringResource(R.string.arabic_dhikr_text_label)) },
                        placeholder = { Text(stringResource(R.string.arabic_dhikr_text_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text(stringResource(R.string.name_transliteration_label)) },
                        placeholder = { Text(stringResource(R.string.name_transliteration_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customTargetText,
                        onValueChange = { customTargetText = it.filter { ch -> ch.isDigit() } },
                        label = { Text(stringResource(R.string.target_count_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetVal = customTargetText.toIntOrNull() ?: 100
                        onCreateCustomDhikr(customName, customArabic, targetVal)
                        showCustomDialog = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Dialog for setting custom target count
    if (showCustomTargetDialog) {
        var inputTarget by remember { mutableStateOf(currentTarget.toString()) }

        AlertDialog(
            onDismissRequest = { showCustomTargetDialog = false },
            title = { Text(stringResource(R.string.set_custom_target)) },
            text = {
                OutlinedTextField(
                    value = inputTarget,
                    onValueChange = { inputTarget = it.filter { ch -> ch.isDigit() } },
                    label = { Text(stringResource(R.string.target_number_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = inputTarget.toIntOrNull() ?: 33
                        if (target > 0) {
                            onSelectTarget(target)
                        }
                        showCustomTargetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.apply))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomTargetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun DhikrListItem(
    name: String,
    arabic: String,
    meaning: String,
    target: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = arabic,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 26.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                if (meaning.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.target_label_format, meaning, target),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.selected),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
