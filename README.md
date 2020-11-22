![Android Master CI](https://github.com/OHoussein/android-clean-archi/workflows/Android%20Master%20CI/badge.svg)
![Android Develop CI](https://github.com/OHoussein/android-clean-archi/workflows/Android%20Develop%20CI/badge.svg)

⚠️ This project works only with Android Studio 4

Implementation of the clear architecture using: 
* Koin for dependency injection
* Paging 3
* Coroutine/Flow
* Easy to test
* Github Action


# Architecture

<div  align="center">
<img src="https://github.com/OHoussein/android-clean-archi/blob/develop/design/architecture.svg" alt="architecture" align=center />
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

# The most difficult part
The integration of the paging library with a clean architecture.


# TODO 10%
- [ ] Handling process death with ViewModel saved state
- [ ] Flexible Navigation, maybe with the navigation component
- [ ] Proguard with D8 for Shrinking, code optimization and code obfuscation