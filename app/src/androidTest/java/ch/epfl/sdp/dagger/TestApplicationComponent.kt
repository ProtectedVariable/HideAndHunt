package ch.epfl.sdp.dagger

import android.content.Context
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.dagger.modules.FakeReplayModule
import ch.epfl.sdp.dagger.modules.FakeRepoModule
import ch.epfl.sdp.dagger.modules.FakeUserConnectorModule
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import ch.epfl.sdp.replay.ManageReplaysActivity
import ch.epfl.sdp.replay.ReplayInfoListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FakeRepoModule::class, FakeUserConnectorModule::class, FakeReplayModule::class])
interface TestApplicationComponent : ApplicationComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): TestApplicationComponent
    }

    override fun inject(activity: GameCreationActivity)
    override fun inject(activity: GameLobbyActivity)
    override fun inject(activity: LoginActivity)
    override fun inject(activity: ManageReplaysActivity)
    override fun inject(activity: ReplayInfoListFragment)
}