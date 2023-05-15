/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.GameModelFactory;
import codex.jmeutil.es.components.Color;
import codex.jmeutil.es.components.GameObject;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import com.jme3.math.FastMath;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class FlatPanelBuilder implements PanelBatchBuilder {
	
	Integer level;
	float discard = -1f;
	
	public FlatPanelBuilder() {}
	public FlatPanelBuilder(Integer level) {
		this.level = level;
	}
	public FlatPanelBuilder(float discard) {
		this.discard = discard;
	}
	public FlatPanelBuilder(Integer level, float discard) {
		this.level = level;
		this.discard = discard;
	}

	@Override
	public boolean discardPanel(PanelBatchBuilder.BuildTools tools) {
		return FastMath.rand.nextFloat() < discard;
	}
	@Override
	public EntityId createPanelEntity(PanelBatchBuilder.BuildTools tools) {
		if (level != null && tools.thread.getLevel() != level) {
			tools.thread.setLevel(level);
		}
		EntityId id = tools.global.ed.createEntity();
		tools.global.ed.setComponents(id,
				new GameObject("panel"),
				new Position(tools.location),
				new Visual(GameModelFactory.PANEL, "game_collision"),
				new Size(tools.global.size.x, tools.global.size.y, 0f),
				new Color(tools.thread.getColorRGBA()));
		return id;
	}
	
}
