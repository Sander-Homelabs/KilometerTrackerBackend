package di

import BCryptPasswordHasher
import JwtTokenService
import PasswordHasher
import TokenService
import org.koin.dsl.module

val securityModule = module {
    single<PasswordHasher> { BCryptPasswordHasher() }
    single<TokenService> { JwtTokenService(get()) }
}