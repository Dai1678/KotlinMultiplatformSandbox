package dev.dai.kotlinmultiplatformsandbox.android.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.dai.kotlinmultiplatformsandbox.android.ColorNoData
import dev.dai.kotlinmultiplatformsandbox.android.ColorSuccessful
import dev.dai.kotlinmultiplatformsandbox.android.ColorUnSuccessful
import dev.dai.kotlinmultiplatformsandbox.android.MyApplicationTheme
import dev.dai.kotlinmultiplatformsandbox.android.R

@Composable
fun LaunchItem(
    missionName: String,
    launchSuccess: Boolean?,
    launchYear: Int,
    details: String?,
    modifier: Modifier = Modifier
) {
    val launchSuccessText = stringResource(
        id = if (launchSuccess != null) {
            if (launchSuccess) {
                R.string.successful
            } else {
                R.string.unsuccessful
            }
        } else {
            R.string.no_data
        }
    )
    val launchSuccessTextColor = if (launchSuccess != null) {
        if (launchSuccess) {
            ColorSuccessful
        } else {
            ColorUnSuccessful
        }
    } else {
        ColorNoData
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(id = R.string.mission_name_field, missionName))
            Text(
                text = launchSuccessText,
                color = launchSuccessTextColor
            )
            Text(text = stringResource(id = R.string.launch_year_field, launchYear))
            Text(text = stringResource(id = R.string.details_field, details.orEmpty()))
        }
    }
}

@Preview
@Composable
private fun LaunchItemPreview() {
    MyApplicationTheme {
        Surface {
            LaunchItem(
                missionName = "FalconSat",
                launchSuccess = false,
                launchYear = 2006,
                details = "Engine failure at 33 seconds and loss of vehicle"
            )
        }
    }
}
