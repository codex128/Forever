/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package codex.infrunner;

/**
 *
 * @author gary
 */
public enum InterpolationMode {
	
	LINEAR("linear"),
	LERP("lerp"),
	ACCELERATE("accelerate");
	
	private final String name;
	
	private InterpolationMode(String name) {
		this.name = name;
	}
	
}
