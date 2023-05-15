/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.KillThreshold;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.components.Position;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author gary
 */
public class ThresholdKillState extends ESAppState {

	EntitySet set;
	
	@Override
	protected void init(Application app) {
		set = ed.getEntities(Position.class, KillThreshold.class);
	}
	@Override
	protected void cleanup(Application app) {
		set.release();
		set = null;
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		if (set.applyChanges()) for (Entity e : set) {
			if (e.get(KillThreshold.class).getThreshold().isBeyond(e.get(Position.class).getPosition())) {
				ed.removeEntity(e.getId());
			}
		}
	}
	
}
