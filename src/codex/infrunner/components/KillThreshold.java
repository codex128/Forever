/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.components;

import codex.jmeutil.math.Threshold3f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author gary
 */
public class KillThreshold implements EntityComponent {
	
	private final Threshold3f threshold = new Threshold3f();
	
	public KillThreshold(Threshold3f threshold) {
		this.threshold.set(threshold);
	}
	
	public Threshold3f getThreshold() {
		return threshold;
	}
	@Override
	public String toString() {
		return "KillThreshold["+threshold+"]";
	}
	
}
