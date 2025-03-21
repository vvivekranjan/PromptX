package com.example.promptx.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.promptx.R

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptXMainScreen(
    navController: NavController,
    viewModel: GenerationViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var promptText by remember { mutableStateOf("") }
    var expandedSection by remember { mutableStateOf<String?>(null) } // Tracks which section is expanded
    val selectedOptions = remember { mutableStateMapOf<String, String>() }
    var dialogOption by remember { mutableStateOf<Pair<String, List<String>>?>(null) }

    var messages = remember { mutableStateListOf<ChatMessage>() } // (Message, isUser)
    val scrollState = rememberLazyListState()
    var generatePrompt by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1C1C1C), Color(0xFF3A3A3A))
    )

    LaunchedEffect(uiState.generatedPrompt) {
        Log.d("DEBUG", "Updated UI State: ${uiState.generatedPrompt}")
        if(uiState.isLoading) {
            messages.add(ChatMessage("Generating response...", false))
        } else if(uiState.error != null) {
            messages.add(ChatMessage("Error: ${uiState.error}", false))
        }
        if(uiState.generatedPrompt.isNotBlank() && !uiState.isLoading && uiState.error == null && messages.lastOrNull()?.text != uiState.generatedPrompt) {
            messages.add(ChatMessage(uiState.generatedPrompt, false))
        }
    }

//    LaunchedEffect(messages.size) {
//        scrollState.animateScrollToItem(messages.lastIndex)
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "PROMPTX",
//                        color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(
                            onClick = { }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_google),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color.Transparent)
                            )
//                        Canvas(
//                            onDraw = {
//                                drawCircle(imageBrush)
//                            },
//                            modifier = Modifier.size(30.dp)
//                        )
                        }
                    },
                )
            },
            containerColor = Color.Transparent,
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFF2B2B2B),
                    contentColor = Color.White,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        placeholder = { Text("Enter your Topic...") },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        trailingIcon = {
                            Row {
                                IconButton(
                                    onClick = { showSheet = true },
                                    enabled = promptText.isNotEmpty()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Customize Prompt",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedTextColor = Color.White,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White
                        )
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        state = scrollState
                    ) {
                        items(messages) {
                            ChatBubble(it, LocalClipboardManager.current, { viewModel.copyPrompt() })
                        }
                    }
                }

                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = sheetState,
                        containerColor = Color(0xFF2B2B2B)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                "Customize Your Prompt",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            CustomizationSection(
                                title = "Basic Settings",
                                options = listOf(
                                    "Topic" to listOf("Technology", "Health", "Business", "Education"),
                                    "Tone" to listOf("Formal", "Casual", "Persuasive", "Humorous"),
                                    "Length" to listOf("Short", "Medium", "Long"),
                                    "Audience" to listOf("General", "Experts", "Beginners"),
                                    "Keywords" to listOf("Auto-detect", "User-defined"),
                                    "Output Format" to listOf("Article", "Blog Post", "Email", "List")
                                ),
                                isPremium = false,
                                expandedSection = expandedSection,
                                onExpand = { section -> expandedSection = if (expandedSection == section) null else section },
                                selectedOptions = selectedOptions,
                                onOptionClick = { option, choices -> dialogOption = option to choices }
                            )

                            CustomizationSection(
                                title = "Advanced Settings (Premium) \uD83D\uDD12",
                                options = listOf(
                                    "Creativity Level" to emptyList(),
                                    "Writing Style" to emptyList(),
                                    "Context & Background Info" to emptyList(),
                                    "SEO Optimization" to emptyList(),
                                    "Language & Tone Adjustment" to emptyList(),
                                    "Multiple Variations" to emptyList()
                                ),
                                isPremium = true,
                                expandedSection = expandedSection,
                                onExpand = { section -> expandedSection = if (expandedSection == section) null else section },
                                selectedOptions = selectedOptions,
                                onOptionClick = { option, choices -> dialogOption = option to choices }
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    val selectedSettings = buildPromptGenerationInstructions(promptText, selectedOptions)
                                    generatePrompt = "Prompt: $promptText\nSettings: $selectedSettings"
                                    viewModel.updateInputText(promptText)
                                    viewModel.generatePrompt(selectedOptions.toMap())
                                    messages.add(ChatMessage(generatePrompt, true))
                                    showSheet = false
                                    promptText = ""
                                          },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Apply & Generate")
                            }
                        }
                    }
                }
            }
        }
        dialogOption?.let { (option, choices) ->
            OptionSelectionDialog(
                title = option,
                choices = choices,
                selectedOption = selectedOptions[option] ?: choices.first(),
                onOptionSelected = { selectedOptions[option] = it },
                onDismiss = { dialogOption = null }
            )
        }
    }
}

@Composable
fun CustomizationSection(
    title: String,
    options: List<Pair<String, List<String>>>,
    isPremium: Boolean,
    expandedSection: String?,
    onExpand: (String) -> Unit,
    selectedOptions: MutableMap<String, String>,
    onOptionClick: (String, List<String>) -> Unit
) {
    val sectionKey = title // Unique key for each section

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(sectionKey) }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = if (expandedSection == sectionKey) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse",
                    tint = Color.White
                )
            }

            AnimatedVisibility(visible = expandedSection == sectionKey) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    options.forEach { (option, choices) ->
                        if (choices.isNotEmpty()) {
                            DialogOption(option, choices, selectedOptions[option] ?: choices.first(), onOptionClick)
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(option, fontWeight = FontWeight.SemiBold)
                                if (isPremium) {
                                    Text("Premium \uD83D\uDD12", color = Color.Yellow)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogOption(
    title: String,
    choices: List<String>,
    selectedOption: String,
    onOptionClick: (String, List<String>) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOptionClick(title, choices) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Medium)
        Text(selectedOption, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun OptionSelectionDialog(
    title: String,
    choices: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                choices.forEach { choice ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(choice)
                                onDismiss()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = choice == selectedOption,
                            onClick = {
                                onOptionSelected(choice)
                                onDismiss()
                            }
                        )
                        Text(choice, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    clipboardManager: ClipboardManager,
    OnCopy: () -> Unit
) {
    val context = LocalContext.current
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = if (message.isUser) Color(0xFF007AFF) else Color(0xFF3A3A3A),
                        shape = RoundedCornerShape(
                            topStart = 16.dp, topEnd = 16.dp,
                            bottomEnd = if (message.isUser) 0.dp else 16.dp,
                            bottomStart = if (message.isUser) 16.dp else 0.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(text = message.text, color = Color.White)
            }
        }
        IconButton(
            onClick = {
                clipboardManager.setText(AnnotatedString(message.text))
                OnCopy()
                Toast.makeText(context, "Text Copied!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPromptXMainScreen() {
    PromptXMainScreen(rememberNavController())
}

fun buildPromptGenerationInstructions(promptText: String, selectedOptions: Map<String, String>): String {
    val instructionsBuilder = StringBuilder()

    instructionsBuilder.append("Generate a prompt about: '$promptText'. ")
    instructionsBuilder.append("Please adhere to the following specifications:\n")

    selectedOptions.forEach { (key, value) ->
        instructionsBuilder.append("- $key: $value\n")
    }

    instructionsBuilder.append("Craft the prompt to be clear, concise, and tailored to these specific requirements.")

    return instructionsBuilder.toString()
}
