/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.GameModelFactory;
import codex.infrunner.panels.PanelBatchBuilder;
import codex.jmeutil.es.components.Color;
import codex.jmeutil.es.components.GameObject;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import codex.jmeutil.math.IDomain;
import com.jme3.math.FastMath;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class SimplePanelBuilder implements PanelBatchBuilder {
	
	IDomain levelDomain = new IDomain(-1, 1);
	float discard = .3f;
	float flat = .9f;
	float up = .5f;
	
	@Override
	public boolean discardPanel(BuildTools tools) {
		if (tools.global.step > 0) {
			return FastMath.rand.nextFloat() < discard;
		}
		else {
			return false;
		}
	}
	@Override
	public EntityId createPanelEntity(BuildTools tools) {
		PanelBatchBuilder.GlobalBuildTools global = tools.global;
		EntityId id = global.ed.createEntity();
		float flatchance = FastMath.rand.nextFloat();
		if ((flatchance < flat
				|| (tools.thread.getLevel() == levelDomain.getMin()
					&& tools.thread.getLevel() == levelDomain.getMax()))
				&& global.step > 0) {
			global.ed.setComponents(id,
				new GameObject("panel"),
				new Position(tools.location),
				new Size(global.size.x, global.size.y, 0f),
				new Visual(GameModelFactory.PANEL, "game_collision"),
				new Color(tools.thread.getColorRGBA()));
		}
		else {
			float upchance = FastMath.rand.nextFloat();
			if ((upchance > up || tools.thread.getLevel() == levelDomain.getMax())
					&& tools.thread.getLevel() > levelDomain.getMin() && global.step > 0) {
				tools.thread.setLevel(tools.thread.getLevel()-1);
				global.ed.setComponent(id, new Size(global.size.x, global.size.y, -global.size.z));				
			}
			else {
				tools.thread.setLevel(tools.thread.getLevel()+1);
				global.ed.setComponent(id, new Size(global.size));
			}
			global.ed.setComponents(id,
				new GameObject("panel"),
				new Position(tools.location),
				new Visual(GameModelFactory.RAMP, "game_collision"),
				new Color(tools.thread.getColorRGBA()));
		}
		return id;
	}
	
}
