/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.naz.ep2

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class VideoActivity : AppCompatActivity(), AnkoLogger {

    lateinit var exoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
    }

    override fun onResume() {
        super.onResume()
        initPlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    fun initPlayer() {
        // Create the player
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        // Bind to the view
        exoplayerview_activity_video.player = exoPlayer

        // Pick the media to play
        val userAgent = Util.getUserAgent(this, this.javaClass.simpleName)

        val uri = Uri.parse("file:///android_asset/video/stock_footage_video.mp4")
        //val uri = Uri.parse("asset:///video/stock_footage_video.mp4")

        val mediaSource = ExtractorMediaSource
                .Factory(DefaultDataSourceFactory(this, userAgent))
                .createMediaSource(uri)

        // Tell the player to play the media
        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = true

        // Attach logging
        exoPlayer.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                info { "playerStateChanged: ${getStateString(playbackState)}, $playWhenReady" }
            }

            fun getStateString(state: Int): String {
                when (state) {
                    Player.STATE_BUFFERING -> return "STATE_BUFFERING"
                    Player.STATE_ENDED -> return "STATE_ENDED"
                    Player.STATE_IDLE -> return "STATE_IDLE"
                    Player.STATE_READY -> return "STATE_READY"
                    else -> return "?"
                }
            }
        })
    }

    fun releasePlayer() = exoPlayer.release()

}
