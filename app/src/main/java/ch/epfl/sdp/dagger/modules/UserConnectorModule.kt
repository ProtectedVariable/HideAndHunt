package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.authentication.FirebaseUserConnector
import ch.epfl.sdp.authentication.IUserConnector
import dagger.Module
import dagger.Provides

@Module
class UserConnectorModule {
    @Provides
    fun providesUserConnector(): IUserConnector {
        return FirebaseUserConnector()
    }
}