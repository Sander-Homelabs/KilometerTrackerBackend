package di

import EmailService
import org.koin.dsl.module

val emailModule = module {
    single { EmailService() }
}