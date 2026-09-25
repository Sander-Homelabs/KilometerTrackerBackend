package di

import BCryptPasswordHasher
import PasswordHasher
import org.koin.dsl.module

val securityModule = module {
    single<PasswordHasher> {
        BCryptPasswordHasher()
    }
}