package com.partha.cookingapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.partha.cookingapp.R
import com.partha.cookingapp.pojos.Dish
import com.partha.cookingapp.ui.theme.CookingAppTheme
import com.partha.cookingapp.ui.theme.DarkBlue
import com.partha.cookingapp.uitls.ScheduleCookingTimeBottomSheet
import com.partha.cookingapp.viewmodels.MainActivityViewModel


class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.fetchDishes()
        setContent {
            CookingAppTheme {
                CookingApp(windowSize = calculateWindowSizeClass(this), viewModel)
            }
        }

    }
}

@Composable
fun CookingApp(windowSize: WindowSizeClass, viewModel: MainActivityViewModel) {
    val selectedIndex = rememberSaveable { mutableIntStateOf(-1) }
    val showBottomSheet = rememberSaveable { mutableStateOf(false) }

    if (showBottomSheet.value){
        ScheduleCookingTimeBottomSheet(onDismissRequest = {
            showBottomSheet.value = false
        })
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CookingAppPortrait(viewModel, selectedIndex, showBottomSheet)
        }
        WindowWidthSizeClass.Expanded -> {
            CookingAppLandscape(viewModel, selectedIndex, showBottomSheet)
        }
    }
}

@Composable
fun CookingAppPortrait(viewModel: MainActivityViewModel, selectedIndex: MutableIntState, showBottomSheet: MutableState<Boolean>) {
    Scaffold(bottomBar = { BottomNavigation() }) { padding ->
        HomeScreen(Modifier.padding(padding), viewModel, selectedIndex, showBottomSheet)
    }
}

@Composable
fun CookingAppLandscape(viewModel: MainActivityViewModel, selectedIndex: MutableIntState, showBottomSheet: MutableState<Boolean>){
    Scaffold { padding ->
        Row {
            CookingNavigationRail()
            HomeScreen(modifier = Modifier.padding(padding), viewModel = viewModel, selectedIndex, showBottomSheet)
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: MainActivityViewModel, selectedIndex: MutableIntState, showBottomSheet: MutableState<Boolean>) {
    val dishes by viewModel.dishes.observeAsState(initial = emptyList())
    val error by viewModel.error.observeAsState(initial = null)

    Column(modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(modifier = Modifier.padding(horizontal = 16.dp))
        HomeSection(title = R.string.whats_on_your_mind) {
            WhatsOnYourMindRow()
        }
        HomeSection(title = R.string.recommendation) {
            RecommendationRow(dishes = dishes, selectedIndex = selectedIndex, showBottomSheet = showBottomSheet)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    var searchText by rememberSaveable { mutableStateOf("") }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(CircleShape)
            .border(1.dp, DarkBlue, CircleShape),
        value = searchText,
        onValueChange = { searchText = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = DarkBlue
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        }
    )
}


@Composable
fun HomeSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium,
            color = DarkBlue,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

@Composable
fun WhatsOnYourMindRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(whatsOnYourMindData) { item ->
            WhatsOnYourMindCard(drawableId = item.drawable, itemName = item.text)
        }
    }
}


@Composable
fun WhatsOnYourMindCard(
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int,
    itemName: String
) {
    Card(
        shape = CircleShape,
        modifier = modifier.shadow(8.dp, CircleShape),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xfffafafc)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(2.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(
                text = itemName,
                style = MaterialTheme.typography.titleMedium,
                color = DarkBlue,
                modifier = Modifier.padding(start = 10.dp, end = 16.dp)
            )
        }
    }
}


@Composable
fun RecommendationRow(
    modifier: Modifier = Modifier,
    dishes: List<Dish>,
    selectedIndex: MutableIntState,
    showBottomSheet: MutableState<Boolean>
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(dishes){ index, item ->
            RecommendationCard(item, index, selectedIndex, showBottomSheet)
        }
    }
}

@Composable
fun RecommendationCard(dish: Dish, index: Int, selectedIndex: MutableIntState, showBottomSheet: MutableState<Boolean>) {
    var isClicked = index == selectedIndex.intValue


    // Colors based on the clicked state
    val backgroundColor = if (isClicked) DarkBlue else Color(0xfffafafc)
    val textColor = if (isClicked) Color.White else Color.Black


    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(5.dp)
            .clickable {
                showBottomSheet.value = true
                isClicked = !isClicked
                selectedIndex.intValue = if (isClicked) index else -1
            }
            .shadow(8.dp, RoundedCornerShape(16.dp), clip = false),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            // Image and Rating Section Container
            Box {
                // Image Section
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color(0xffebebeb))
                ) {
                    AsyncImage(
                        model = dish.imageUrl,
                        contentDescription = dish.dishName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .size(115.dp)
                    )
                }

                // Rating Section - Centered at the bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 8.dp)
                        .background(
                            color = Color(0xFFFFA500),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating Star",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 3.dp)
                    )
                    Text(
                        "4.2",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                }
            }

            // Title
            Text(
                text = dish.dishName?:"NA",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isClicked) textColor else DarkBlue,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 15.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Description Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AvTimer,
                    contentDescription = "Time",
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    "30 min · Medium prep.",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomNavigation(modifier: Modifier = Modifier) {
    NavigationBar (
        modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ){
        NavigationBarItem(
            selected = true, onClick = { },
            icon = {
                Icon(imageVector = Icons.Default.Spa, contentDescription = null)
            },
            label = {
                Text(text = stringResource(R.string.bottom_navigation_home))
            }
        )
        NavigationBarItem(
            selected = false, onClick = {  },
            icon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            },
            label = {
                Text(text = stringResource(id = R.string.bottom_navigation_profile))
            }
        )
    }
}


@Composable
private fun CookingNavigationRail(modifier: Modifier = Modifier) {
    NavigationRail(
        modifier = modifier.padding(start = 8.dp, end = 8.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = DarkBlue
                    )
                },
                label = {
                    Text(stringResource(
                        R.string.bottom_navigation_home),
                        color = DarkBlue
                    )
                },
                selected = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(5.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = DarkBlue
                    )
                },
                label = {
                    Text(
                        text = "Favorites",
                        color = DarkBlue
                    )
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(5.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = DarkBlue
                    )
                },
                label = {
                    Text(
                        text = "Manual",
                        color = DarkBlue
                    )
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(5.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Devices,
                        contentDescription = null,
                        tint = DarkBlue
                    )
                },
                label = {
                    Text(
                        text = "Device",
                        color = DarkBlue
                    )
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(5.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = DarkBlue
                    )
                },
                label = {
                    Text(
                        text = "Preferences",
                        color = DarkBlue
                    )
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(5.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = DarkBlue
                    )
                },
                label = {
                    Text(
                        text = "Settings",
                        color = DarkBlue
                    )
                },
                selected = false,
                onClick = {}
            )
        }
    }
}

private val whatsOnYourMindData = listOf(
    R.drawable.rice to "Rice items",
    R.drawable.indian_food to "Indian",
    R.drawable.curries to "Curries",
    R.drawable.soups to "Soups",
    R.drawable.curries to "Desserts",
    R.drawable.indian_food to "Snacks",
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    val text: String
)

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun SearchBarPreview() {
    CookingAppTheme { SearchBar(Modifier.padding(8.dp)) }
}



@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun WhatsOnYourMindCardPreview() {
    CookingAppTheme {
        WhatsOnYourMindCard(
            modifier = Modifier.padding(8.dp),
            itemName = "Rice items",
            drawableId = R.drawable.rice
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendationsPreview() {
    RecommendationCard(Dish(
        dishName = "Jeera Rice",
        dishId = "1",
        imageUrl = "https://nosh-assignment.s3.ap-south-1.amazonaws.com/jeera-rice.jpg",
        isPublished = true
    ), 0, remember { mutableIntStateOf(-1) }, remember { mutableStateOf(false) })
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun BottomNavigationPreview() {
    CookingAppTheme {
        BottomNavigation(Modifier.padding(top = 24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun NavigationRailPreview() {
    CookingAppTheme { CookingNavigationRail() }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun CookingAppPortraitPreview() {
    CookingAppPortrait(MainActivityViewModel(), remember { mutableIntStateOf(-1) }, remember { mutableStateOf(false) })
}

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun CookingAppLandscapePreview() {
    CookingAppLandscape(MainActivityViewModel(), remember { mutableIntStateOf(-1)}, remember { mutableStateOf(false) })
}