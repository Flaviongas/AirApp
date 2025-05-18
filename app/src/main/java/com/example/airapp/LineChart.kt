package com.example.airapp

/*
 * Copyright 2025 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.runBlocking

@Composable
private fun JetpackComposeBasicLineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    repeatedDays: List<String>
) {
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(label = rememberAxisLabelComponent(color= Color.White)),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = rememberAxisLabelComponent(color = Color.White),
                valueFormatter = { _, value, _ ->
                    repeatedDays.getOrNull(value.toInt() - 1) ?: ""
                }
            ),


            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun JetpackComposeBasicLineChart(modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val daysOfWeek = listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")
    val repeatedDays = (1..16).map { index -> daysOfWeek[(index - 1) % 7] } // Repeat for 16 days

    // 2. Random Y-values (between 0 and 20)
    val randomYValues = List(16) { (0..20).random() }

    // 3. Numeric X-values (1, 2, 3, ...)
    val xValues = (1..16).map { it.toFloat() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/vmml6t.

            lineSeries { series(x = xValues, y = randomYValues) }

        }
    }
    JetpackComposeBasicLineChart(modelProducer, modifier.padding(all = 5.dp),repeatedDays)
}
