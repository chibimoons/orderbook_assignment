package com.junyoung.ha.features.orderbook.ui

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.junyoung.ha.common.blocviewmodel.LaunchInitialAction
import com.junyoung.ha.features.common.domain.Price
import com.junyoung.ha.features.common.domain.toFormattedString
import com.junyoung.ha.features.common.domain.toQuantityFormat
import com.junyoung.ha.features.orderbook.domain.model.OrderBook
import com.junyoung.ha.features.orderbook.domain.model.OrderInfo
import com.junyoung.ha.features.orderbook.domain.model.OrderType
import com.junyoung.ha.features.orderbook.presentation.OrderBookUi
import java.math.BigDecimal

@Composable
fun OrderBookRenderer(viewModel: OrderBookViewModel, modifier: Modifier) {
    Box (modifier) {
        val state by viewModel.stateFlow.collectAsState()
        LaunchInitialAction(
            initialState = OrderBookUi.State.INITIAL_STATE,
            initialAction = OrderBookUi.Action.Start,
            viewModel = viewModel,
            curState = state
        )

        Content(state, viewModel::dispatch)
    }
}

@Composable
private fun Content(state: OrderBookUi.State, onAction: (OrderBookUi.Action) -> Unit) {
    val orderBook by state.orderBookFlow.collectAsState(initial = OrderBook.EMPTY)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Header()
        for (i in 0 until orderBook.maxOrderListSize) {
            val buyOrderInfo = orderBook.buyOrderList.orderInfoList.getOrNull(i)
            val sellOrderInfo = orderBook.sellOrderList.orderInfoList.getOrNull(i)
            OrderInfoRow(
                buyOrderInfo = buyOrderInfo,
                buyOrderRelativeQuantity = buyOrderInfo?.let {
                    orderBook.getRelativeQuantity(OrderType.BUY, it.price)
                } ?: 0F,
                sellOrderInfo = sellOrderInfo,
                sellOrderRelativeQuantity = sellOrderInfo?.let {
                    orderBook.getRelativeQuantity(OrderType.SELL, it.price)
                } ?: 0f
            )
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
            HeaderText(text = stringResource(id = R.string.header_label_quantity), modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart)
            HeaderText(text = stringResource(id = R.string.header_label_price), modifier = Modifier.weight(1f), contentAlignment = Alignment.Center)
            HeaderText(text = stringResource(id = R.string.header_label_quantity), modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd)
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

private val orderInfoRowModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp)

@Composable
private fun OrderInfoRow(
    buyOrderInfo: OrderInfo?,
    buyOrderRelativeQuantity: Float,
    sellOrderInfo: OrderInfo?,
    sellOrderRelativeQuantity: Float
) {
    Row(
        modifier = orderInfoRowModifier
    ) {
        BuyOrderInfoRow(buyOrderInfo, buyOrderRelativeQuantity, Modifier.weight(1f))
        SellOrderInfoRow(sellOrderInfo, sellOrderRelativeQuantity, Modifier.weight(1f))
    }
}

@Composable
private fun BuyOrderInfoRow(
    buyOrderInfo: OrderInfo?,
    buyOrderRelativeQuantity: Float,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (buyOrderInfo != null) {
            QuantityText(buyOrderInfo.quantity, Modifier.weight(1f), Alignment.CenterStart)
            BuyPriceText(buyOrderInfo.price, buyOrderRelativeQuantity,
                Modifier
                    .weight(1f)
                    .height(48.dp))
        }
    }
}

@Composable
private fun QuantityText(quantity: BigDecimal, modifier: Modifier, contentAlignment: Alignment) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Text(
            text = quantity.toQuantityFormat(),
            color = Color.Black,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}

@Composable
private fun BuyPriceText(price: Price, relativeQuantity: Float, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = 1f - relativeQuantity,
            trackColor = Color.Green.copy(alpha = 0.1f),
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(end = 2.dp),
            text = price.toFormattedString(),
            color = Color.Green,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}

@Composable
private fun SellOrderInfoRow(
    sellOrderInfo: OrderInfo?,
    sellOrderRelativeQuantity: Float,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (sellOrderInfo != null) {
            SellPriceText(sellOrderInfo.price, sellOrderRelativeQuantity,
                Modifier
                    .weight(1f)
                    .height(48.dp))
            QuantityText(sellOrderInfo.quantity, Modifier.weight(1f), Alignment.CenterEnd)
        }
    }
}

@Composable
private fun SellPriceText(price: Price, relativeQuantity: Float, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = relativeQuantity,
            trackColor = Color.White,
            color = Color.Red.copy(alpha = 0.1f)
        )
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = price.toFormattedString(),
            color = Color.Red,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}