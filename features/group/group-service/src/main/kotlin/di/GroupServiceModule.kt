package di

import AddGroupUserUseCase
import AdminCreateGroupUseCase
import org.koin.dsl.module

val groupServiceModule = module {
    single<AdminCreateGroupUseCase> { AdminCreateGroupUseCase(get(), get()) }
    single<AddGroupUserUseCase> { AddGroupUserUseCase(get(), get(), get(), get(), get()) }
}