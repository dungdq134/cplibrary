package pl.cyfrowypolsat.cpplayercore.events.player

import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltMatchItem

interface PlayerListener {

    fun onPlayerError(e: PlayerException) {

    }

    fun playerInitialized(playerConfig: PlayerConfig) {

    }

    fun onNextEpisodeButtonClicked(isPlayerUILocked: Boolean) {

    }

    fun onNextEpisodeAutoPlay(isPlayerUILocked: Boolean) {

    }

    fun onPositionUpdate(positionMs: Long, duration: Long) {

    }

    fun onAdBlockStarted() {

    }

    fun onAdBlockEnded() {

    }

    fun onSeeAlsoItemClicked(seeAlsoItem: SeeAlsoItem, isPlayerUILocked: Boolean) {

    }

    fun onSeeAlsoItemAutoPlay(seeAlsoItem: SeeAlsoItem, isPlayerUILocked: Boolean) {

    }

    fun onTeravoltItemClicked(teravoltMatchItem: TeravoltMatchItem) {

    }

}
