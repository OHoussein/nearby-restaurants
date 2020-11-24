![Android Master CI](https://github.com/OHoussein/nearby-restaurants/workflows/Android%20Master%20CI/badge.svg)


<div  align="center">
<img src="https://github.com/OHoussein/nearby-restaurants/blob/master/design/screenshot.png" alt="architecture" align=center />
</div>

An app that shows the restaurant around you using:
* The clean architecture 
* Dagger for dependency injection
* Coroutine/Flow
* Easy to test architecture
* buildSrc
* Github Action
* Mapbox
* Foursquare API

# Build the project
1. At your developer account at foursquare and copy your client ID and client secret in the secure.gradle file
1. Copy your mapbox client id into secure.gradle
1. Copy your mapbox secret id into secure.gradle


# Architecture

<div  align="center">
<img src="https://github.com/OHoussein/nearby-restaurants/blob/master/design/architecture.png" alt="architecture" align=center />
</div>

## Flow

1. UI (Fragment, Activity, ...) calls loading method from the ViewModel.
1. ViewModels calls the PagingDataSource.
1. The PagingDataSource execute the use case.
1. Use case combines data from local and remote Repositories.
1. The repository returns data from a remote or local data source (Retrofit, DAO, ...).
1. Information flows back to the UI.

## Layers
### Domain Layer 
This module should contain only Java/Kotlin classes and doesn't have any Android framework class.
It defines the business aspect of the application. With the use case it combines data from the data layer
It doesn't depend on any module. It communicates with the data module through a repository interface  declared on the domain layer and implemented on the data layer.
### Presentation Layer
This is the plateform specific layer, it contains UI coordinated by ViewModels which execute use cases.
This layer depends on the Domain Layer. With the DomainModelMappers, domains models are converted to ui models.
### Data Layer
Contains remote, stored or in-memory data sources.
With the DomainModelMappers, data models like api models or database model are converted to domain models.

# Libraries
* **Dagger Hilt :** for the dependency injection, this library is so simple to use and to make testable code. 
* **Coroutine :** doing async tasks is so simple with this new style of concurency
* **Flow :** for handling multi-shots data with coroutine with async collections transformation (a lot of its API still in experimental)
* **Github Action :** For quick CI integration  and full integration with Github


# TODO
- [ ] Handle process death with ViewModel saved state
- [ ] Create the UI tests
- [ ] Fix the uni tests caused by the flow dispatcher