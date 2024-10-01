package com.flowtrust.windychatty.ui.pages.auth.contry_picker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation(val mask: String,val colorMask: Color,val colorDigit:Color) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val maskedText = buildMaskedText(text.text, mask)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return transformOffset(offset, mask, text.text.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return originalOffset(offset, mask, text.text.length)
            }
        }

        return TransformedText(maskedText, offsetMapping)
    }

    // Функция для применения маски к введенному тексту с разными стилями
    private fun buildMaskedText(input: String, mask: String): AnnotatedString {
        val result = AnnotatedString.Builder()
        var inputIndex = 0

        val maxInputLength = mask.count { it == '0' }

        for (maskChar in mask) {
            if (maskChar == '0') {
                if (inputIndex < input.length && inputIndex < maxInputLength) {
                    result.pushStyle(SpanStyle(color = colorDigit)) // Стиль для введенных пользователем цифр
                    result.append(input[inputIndex].toString())
                    result.pop()
                    inputIndex++
                } else {
                    result.pushStyle(SpanStyle(color = colorMask)) // Стиль для нулей из маски
                    result.append(maskChar)
                    result.pop()
                }
            } else {
                result.pushStyle(SpanStyle(color = colorMask)) // Стиль для символов маски
                result.append(maskChar.toString())
                result.pop()
            }
        }

        return result.toAnnotatedString()
    }

    // Преобразование оригинального смещения в смещение с маской
    private fun transformOffset(offset: Int, mask: String, textLength: Int): Int {
        var countOfDigits = 0
        var resultOffset = 0

        while (countOfDigits < offset && resultOffset < mask.length) {
            if (mask[resultOffset] == '0') {
                countOfDigits++
            }
            resultOffset++
        }
        val maxInputLength = mask.length
        return minOf(resultOffset,maxInputLength)
    }

    // Преобразование смещения с маской в оригинальное смещение
    private fun originalOffset(offset: Int, mask: String, textLength: Int): Int {
        var countOfDigits = 0
        var resultOffset = 0

        // Проходим по маске и учитываем только цифры
        while (resultOffset < offset && resultOffset < mask.length) {
            if (mask[resultOffset] == '0') {
                countOfDigits++
            }
            resultOffset++
        }

        // Если смещение больше длины введенного текста, возвращаем максимальный возможный индекс
        return minOf(countOfDigits, textLength)
    }
}

