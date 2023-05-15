/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author gary
 */
public class Decay implements EntityComponent {
	
	private final float life;
	
	public Decay(float life) {
		this.life = life;
	}
	
	public float getLife() {
		return life;
	}
	@Override
	public String toString() {
		return "Decay["+life+"]";
	}
	
}
