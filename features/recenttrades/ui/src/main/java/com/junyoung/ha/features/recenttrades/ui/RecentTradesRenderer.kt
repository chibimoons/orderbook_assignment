package com.junyoung.ha.features.recenttrades.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.junyoung.ha.common.blocviewmodel.LaunchInitialAction
import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.common.domain.toFormattedString
import com.junyoung.ha.features.common.domain.toQuantityFormat
import com.junyoung.ha.features.recenttrades.domain.model.RecentTrades
import com.junyoung.ha.features.recenttrades.domain.model.TradeInfo
import com.junyoung.ha.features.recenttrades.domain.model.TradeType
import com.junyoung.ha.features.recenttrades.presentation.RecentTradesUi
import com.junyoung.hafeatures.recenttrade.ui.R
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun RecentTradesRenderer(
    viewModel: RecentTradesViewModel,
    modifier: Modifier
) {
    Box(modifier) {
        val state by viewModel.stateFlow.collectAsState()
        LaunchInitialAction(
            initialState = RecentTradesUi.State.INITIAL_STATE,
            initialAction = RecentTradesUi.Action.Start,
            viewModel = viewModel,
            curState = state
        )

        Content(state, viewModel::dispatch)
    }
}

@Composable
private fun Content(state: RecentTradesUi.State, onAction: (RecentTradesUi.Action) -> Unit) {
    val recentTrades by state.recentTradesFlow.collectAsState(initial = RecentTrades.EMPTY)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Header()
        recentTrades.tradeInfoList.forEach {
            TradeInfoRow(tradeInfo = it, recentTrades.isNew(it.id))
        }
    }
}


@Composable
private fun Header() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            HeaderText(text = stringResource(id = R.string.header_label_price), modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart)
            HeaderText(text = stringResource(id = R.string.header_label_quantity), modifier = Modifier.weight(1f))
            HeaderText(text = stringResource(id = R.string.header_label_time), modifier = Modifier.weight(1f))
        }
        Divider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.8f))
    }
}

@Composable
private fun HeaderText(text: String, modifier: Modifier, contentAlignment: Alignment = Alignment.CenterEnd) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Text(
            text = text,
            color = Color.Gray
        )
    }
}

@Composable
private fun TradeInfoRow(tradeInfo: TradeInfo, isNew: Boolean) {
    val backgroundColor = remember(tradeInfo) {
        Animatable(
            if (isNew) {
                getTradeTypeColor(tradeType = tradeInfo.tradeType).copy(alpha = 0.1f)
            } else {
                Color.White
            }
        )
    }

    LaunchedEffect(tradeInfo) {
        if (isNew) {
            backgroundColor.animateTo(Color.White, animationSpec = tween(200))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(backgroundColor.value)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        PriceText(tradeType = tradeInfo.tradeType, price = tradeInfo.price, modifier = Modifier.weight(1f))
        QuantityText(tradeType = tradeInfo.tradeType, quantity = tradeInfo.quantity, modifier = Modifier.weight(1f))
        TradeAtText(tradeType = tradeInfo.tradeType, tradeAt = tradeInfo.tradeAt, modifier = Modifier.weight(1f))
    }
}


private fun getTradeTypeColor(tradeType: TradeType): Color {
    return when(tradeType) {
        TradeType.SELL -> Color.Red
        TradeType.BUY -> Color.Green
    }
}

@Composable
private fun PriceText(tradeType: TradeType, price: Price, modifier: Modifier) {
    Box(modifier) {
        Text(
            text = price.toFormattedString(),
            color = getTradeTypeColor(tradeType),
        )
    }
}

@Composable
private fun QuantityText(tradeType: TradeType, quantity: BigDecimal, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = quantity.toQuantityFormat(),
            color = getTradeTypeColor(tradeType)
        )
    }
}

@Composable
private fun TradeAtText(tradeType: TradeType, tradeAt: LocalDateTime, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = tradeAt.format(DateTimeFormatter.ofPattern("hh:mm:ss")),
            color = getTradeTypeColor(tradeType)
        )
    }
}