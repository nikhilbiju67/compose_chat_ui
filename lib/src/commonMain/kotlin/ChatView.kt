import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ChatView() {
    var list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    Scaffold(modifier = Modifier.background(Color.Red))
    {
        EndlessLazyColumn(items = list, itemKey = { it }, itemContent = {
            Text(it)
        }, loading = false, loadingItem = {
            Box(Modifier.fillMaxHeight()) {
                Text("Loading...")
            }
        }, listState = rememberLazyListState(), loadMore = {
            println("load more")
        })
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun <T> EndlessLazyColumn(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    listState: LazyListState = rememberLazyListState(),
    items: List<T>,
    itemKey: (T) -> String,
    itemContent: @Composable (T) -> Unit,
    loadingItem: @Composable () -> Unit,
    loadMore: () -> Unit
) {

    val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom() } }

    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom && !loading) loadMore()
    }
    LaunchedEffect(items) {
        ///if scroll position is zero animate to 0
        if (!listState.canScrollBackward)
            listState.animateScrollToItem(0)
    }


    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(bottom = 64.dp),
        state = listState,
        reverseLayout = true
    ) {
        items.forEach { item ->
            item(key = itemKey(item)) {
                itemContent(item)
            }
        }
        if (loading) {
            item {
                loadingItem()
            }
        }
    }
}
private fun LazyListState.reachedBottom(): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - 1
}

