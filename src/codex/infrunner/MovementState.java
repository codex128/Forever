/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Movement;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.components.Position;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author gary
 */
public class MovementState extends ESAppState {

	EntitySet movers;
	
	@Override
	protected void init(Application app) {
		movers = ed.getEntities(
				Position.class,
				Movement.class);
	}
	@Override
	protected void cleanup(Application app) {
		movers.release();
		movers = null;
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		movers.applyChanges();
		Movement move;
		for (Entity e : movers) {
			move = e.get(Movement.class);
			if (move.getMovement().lengthSquared() > 0f) {
				e.set(new Position(e.get(Position.class).getPosition().addLocal(
						move.getMovement().mult(move.useTimePerFrame() ? tpf : 1))));
			}
		}
	}
	
}
