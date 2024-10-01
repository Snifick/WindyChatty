package com.flowtrust.windychatty.ui.pages.auth

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CodeVisualTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // При преобразовании добавляем смещение на 1 (из-за символа +)
                return offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                // При обратном преобразовании отнимаем смещение 1 (из-за символа +)
                return if (offset > 0) offset - 1 else 0
            }
        }

        return TransformedText(
            AnnotatedString("+$text"),
            offsetMapping
        )
    }
}