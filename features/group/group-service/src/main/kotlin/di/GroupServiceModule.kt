package di

import AdminCreateGroupUseCase
import org.koin.dsl.module

val groupServiceModule = module {
    single<AdminCreateGroupUseCase> { AdminCreateGroupUseCase(get(), get()) }
}