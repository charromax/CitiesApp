# Cities Search App

This app displays a list of cities (~200k) with the following features:  
- **Dynamic search**: Filters cities by prefix, case-insensitive.  
- **Persistent favorites**: Saves favorite cities across sessions using `DataStore`.  
- **Adaptive layout**: Switches between list-only or list-with-map views based on device orientation.  
- **Data optimization**: Utilizes `Paging 3` to efficiently handle large datasets.  

## Stack
- **Jetpack Compose**: For UI design.  
- **MVVM with Clean Architecture**: To ensure clear separation of concerns.  
- **Paging 3**: For data pagination.  
- **DataStore**: To manage persistent favorites.  
- **Hilt**: For dependency injection.  
- **Google Maps API**: To display city locations on a map.  
