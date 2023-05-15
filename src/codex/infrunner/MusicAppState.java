/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

/**
 *
 * @author gary
 */
public class MusicAppState extends ESAppState {

	AudioNode music;
	float volume = 1f;
	float volumeFade = 0f;
	
	@Override
	protected void init(Application app) {}
	@Override
	protected void cleanup(Application app) {}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {
		stopMusic();
	}
	@Override
	public void update(float tpf) {
		if (volumeFade != 0f) {
			float vol = music.getVolume()+volumeFade;
			if (vol <= 0f && volumeFade < 0f) {
				volumeFade = 0f;
				music.stop();
				music = null;
				return;
			}
			else if (vol >= volume && volumeFade > 0f) {
				vol = volume;
				volumeFade = 0f;
			}
			music.setVolume(vol);
		}
	}
	
	public AudioNode loadMusic(String musicSource) {
		stopMusic();
		volumeFade = 0f;
		music = new AudioNode(assetManager, musicSource, AudioData.DataType.Stream);
		music.setPositional(false);
		music.setVolume(volume);
		return music;
	}
	public void playMusic() {
		if (music != null) {
			music.play();
		}
	}
	public void stopMusic() {
		if (music != null) {
			music.stop();
		}
	}
	public void pauseMusic() {
		if (music != null) {
			music.pause();
		}
	}
	
	public void setMusicVolume(float volume) {
		if (music != null) music.setVolume(volume);
		this.volume = volume;
	}
	public void setMusicVolumeFade(float volumeFade) {
		this.volumeFade = volumeFade;
	}
	
	public AudioNode getMusic() {
		return music;
	}
	public boolean hasMusic() {
		return music != null;
	}
	
}
