/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.CopyPosition;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.components.Position;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author gary
 */
public class CopyPositionState extends ESAppState {
	
	EntitySet ents;
	
	@Override
	protected void init(Application app) {
		ents = ed.getEntities(CopyPosition.class,
				Position.class);
	}
	@Override
	protected void cleanup(Application app) {}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		ents.applyChanges();
		CopyPosition copy;
		for (Entity e : ents) {
			copy = e.get(CopyPosition.class);
			e.set(new Position(copy.getConstraint().apply(
					ed.getComponent(copy.getCopiedEntity(), Position.class).getPosition(),
					e.get(Position.class).getPosition())));
		}
	}
	
}
