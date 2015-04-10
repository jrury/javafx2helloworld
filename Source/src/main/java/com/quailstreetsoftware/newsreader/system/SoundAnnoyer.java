package com.quailstreetsoftware.newsreader.system;

import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

public class SoundAnnoyer implements EventListener {

	private Boolean soundsEnabled;
	private EventBus eventBus;
	
	public SoundAnnoyer(EventBus eventBus) {
		this.eventBus = eventBus;
		this.soundsEnabled = Boolean.TRUE;
	}

	public void toggleSounds() {
		this.soundsEnabled = !this.soundsEnabled;
	}

	@Override
	public void eventOccurred(NotificationEvent event, HashMap<String, String> arguments) {

		switch(event) {
			case TOGGLE_SOUNDS:
				toggleSounds();
				eventBus.eventReceived(NotificationEvent.DEBUG_MESSAGE, Utility.getParameterMap(
						NotificationParameter.DEBUG_MESSAGE,
						"Sounds are now " + (this.soundsEnabled ? "enabled." : "disabled")));
				break;
			case PLAY_SOUND:
				Runnable r = new Runnable() {
					public void run() {
						String soundFile = this.getClass().getClassLoader().getResource("META-INF/sounds/00000001.wav").toString();
						Media sound = new Media(soundFile);
						MediaPlayer mediaPlayer = new MediaPlayer(sound);
						mediaPlayer.play();
					}
				};
				
				Thread t = new Thread(r);
				t.start();
				break;
			default:
				break;
		}
		
	}

	@Override
	public Boolean interested(NotificationEvent event) {

		switch (event) {
			case TOGGLE_SOUNDS:
				return Boolean.TRUE;
			case PLAY_SOUND:
				return Boolean.TRUE && this.soundsEnabled;
			default:
				return Boolean.FALSE;
		}
	}

}