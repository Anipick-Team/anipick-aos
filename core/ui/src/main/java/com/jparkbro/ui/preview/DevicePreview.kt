package com.jparkbro.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(name = "phone", device = "spec:width=360dp,height=720dp,dpi=480", showBackground = true)
@Preview(name = "foldable", device = "spec:width=728dp,height=1000dp,dpi=480", showBackground = true)
@Preview(name = "tablet", device = "spec:width=800dp,height=1180dp,dpi=480", showBackground = true)
annotation class DevicePreviews
