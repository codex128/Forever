/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Decay;
import codex.jmeutil.es.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author gary
 */
public class DecayState extends ESAppState {
	
	EntitySet decaying;
	
	@Override
	protected void init(Application app) {
		decaying = ed.getEntities(Decay.class);
	}
	@Override
	protected void cleanup(Application app) {}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		decaying.applyChanges();
		Decay decay;
		for (Entity e : decaying) {
			decay = e.get(Decay.class);
			if (decay.getLife() <= 0f) {
				ed.removeEntity(e.getId());
			}
			else {
				e.set(new Decay(decay.getLife()-tpf));
			}
		}
	}
	
}
