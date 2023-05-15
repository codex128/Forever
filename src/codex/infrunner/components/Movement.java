/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author gary
 */
public class Movement implements EntityComponent {
	
	private final Vector3f movement = new Vector3f();
	private boolean useTpf = false;
	
	public Movement() {}
	public Movement(Vector3f movement) {
		this.movement.set(movement);
	}
	public Movement(Vector3f movement, boolean useTpf) {
		this(movement);
		this.useTpf = useTpf;
	}
	
	public Vector3f getMovement() {
		return movement;
	}
	public boolean useTimePerFrame() {
		return useTpf;
	}
	@Override
	public String toString() {
		return "Movement["+movement+"]";
	}
	
}
