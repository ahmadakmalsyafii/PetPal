package com.example.petpal.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Import warna langsung
import com.example.petpal.presentation.theme.PetPalLightGray
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.PetPalDarkGreen

@Composable
fun PetPalTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Label di atas
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BlackText, // Langsung Hitam
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Input Field
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = placeholder, color = GrayText) // Placeholder Abu-abu
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                // Warna Background Abu-abu Terang
                focusedContainerColor = PetPalLightGray,
                unfocusedContainerColor = PetPalLightGray,
                disabledContainerColor = PetPalLightGray,

                // Hilangkan garis bawah (indicator)
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

                // Warna kursor dan teks input
                cursorColor = PetPalDarkGreen,
                focusedTextColor = BlackText,
                unfocusedTextColor = BlackText
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            singleLine = true
        )
    }
}