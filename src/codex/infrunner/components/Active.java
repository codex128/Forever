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
public class Active implements EntityComponent {
	
	private final boolean active;
	
	public Active(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	@Override
	public String toString() {
		return "Active="+active;
	}
	
}
