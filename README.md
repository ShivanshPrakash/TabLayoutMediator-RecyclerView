# TabLayoutMediator-RecyclerView
[![Release](https://jitpack.io/v/ShivanshPrakash/TabLayoutMediator-RecyclerView.svg)](https://jitpack.io/#ShivanshPrakash/TabLayoutMediator-RecyclerView)

A mediator class used to link tabs in TabLayout to items in a RecyclerView. This can be used to map different sections of 
a RecyclerView to a common header represented by a Tab in TabLayout.

<img src="https://s7.gifyu.com/images/ezgif.com-optimized19051394bcde8fe.gif" width="275" height="600" />

## Setup
1. Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
2. Add the dependency

```
dependencies {
    implementation 'com.github.ShivanshPrakash:TabLayoutMediator-RecyclerView:{version}'
}
```

## Usage

1. Create an instance of TabLayoutRecyclerViewMediator
```
val mediator = TabLayoutRecyclerViewMediator(recyclerView,
    headerCount,
    tabLayout,
    headerToItemMapping,
    itemToHeaderMapping) { tab, position ->
    tab.text = categoryList[position]
}
```
2. Call `attach()` function.
```
mediator.attach()
```
The constructor parameters for TabLayoutRecyclerViewMediator are described below -

| Parameter | Description | Type |
| ------------- | ------------- | ------------- |
| `recyclerView`  | RecyclerView used in mediator. | RecyclerView |
| `headerCount`  | Count of headers (tabs).  | Int |
| `tabLayout`  | TabLayout used in mediator.   | TabLayout |
| `headerToItemMapping`  |  Used to map index of tab in `tabLayout` to index of item in `recyclerView`. | ((headerPosition: Int) -> Int? |
| `itemToHeaderMapping`  | Used to map index of item in `recyclerView` to index of tab in `tabLayout`.  | (itemPosition: Int) -> Int? |
| `autoRefresh` | If true `tabLayout` is reset whenever there is a change in adapter attached to `recyclerView`. Default value is true. | Boolean
| `tabConfigurationStrategy`  | Used to configure the tab at the specified position.  | (tab: TabLayout.Tab, position: Int) -> Unit |
