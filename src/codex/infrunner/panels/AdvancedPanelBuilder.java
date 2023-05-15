/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.GameModelFactory;
import codex.infrunner.components.Active;
import codex.jmeutil.es.components.Color;
import codex.jmeutil.es.components.GameObject;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class AdvancedPanelBuilder implements PanelBatchBuilder {
	
	int globalThreadMin = -1;
	int maxThreadGap = 10;
	float real = 1f;
	float discardBoost = 0f;
	
	public AdvancedPanelBuilder() {}
	public AdvancedPanelBuilder(float real) {
		this.real = real;
	}
	public AdvancedPanelBuilder(float real, float discard) {
		this.real = real;
		this.discardBoost = discard;
	}
	
	@Override
	public boolean discardPanel(PanelBatchBuilder.BuildTools tools) {
		if (tools.global.step == 0) return false;
		float discard = FastMath.rand.nextFloat();
		if (tools.thread.getLevel() == tools.global.stats.lowest.getLevel()) {
			return discard < .25f+discardBoost;
		}
		else {
			return discard < .4f+discardBoost;
		}
	}
	@Override
	public EntityId createPanelEntity(PanelBatchBuilder.BuildTools tools) {
		PanelBatchBuilder.GlobalBuildTools global = tools.global;
		EntityId id = global.ed.createEntity();
		boolean heightlimit = global.stats.highest == null || global.stats.lowest == null ||
				global.stats.highest.getLevel()-global.stats.lowest.getLevel() >= maxThreadGap;
		float flatchance = FastMath.rand.nextFloat();
		PanelThread n1 = null, n2 = null;
		if (tools.index > 0) n1 = global.threads[tools.index-1];
		if (tools.index < global.threads.length-1) n2 = global.threads[tools.index+1];
		boolean hasGreaterNeigbor = (n1 != null && n1.getLevel() > tools.thread.getLevel()) || (n2 != null && n2.getLevel() > tools.thread.getLevel());
		if ((flatchance < .8f
				//|| (hasGreaterNeigbor && flatchance < .7f)			
				//|| (tools.thread.getLevel() == lowest.getLevel() && flatchance < .5f)
				&& global.step > 0) || (global.step == 0 && real < 1f)) {
			global.ed.setComponents(id,
				new GameObject("panel"),
				new Position(tools.location),
				new Size(global.size.x, global.size.y, 0f));
			float realchance = FastMath.rand.nextFloat();
			if (realchance < real && global.step > 0) {
				global.ed.setComponents(id,
					new Visual(GameModelFactory.PANEL, "game_collision"),
					new Color(tools.thread.getColorRGBA()));	
			}
			else {
				global.ed.setComponents(id,
					new Visual(GameModelFactory.BROKEN_PANEL),
					new Color(ColorRGBA.Gray),
					new Active(false));
			}
		}
		else {
			float upchance = FastMath.rand.nextFloat();
			boolean up = upchance < .5f+tools.thread.getUpTendency()*.02f ||
					(hasGreaterNeigbor && upchance < .8f);
			if ((heightlimit && tools.thread.getLevel() == global.stats.highest.getLevel()) ||
					(!up && (!heightlimit || tools.thread.getLevel() > global.stats.lowest.getLevel())) &&
					tools.thread.getLevel() != globalThreadMin && global.step > 0) {
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
