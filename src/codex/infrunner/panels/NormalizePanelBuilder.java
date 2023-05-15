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
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class NormalizePanelBuilder implements PanelBatchBuilder {
	
	public NormalizePanelBuilder() {}
	
	@Override
	public boolean discardPanel(PanelBatchBuilder.BuildTools tools) {
		return false;
	}
	@Override
	public EntityId createPanelEntity(PanelBatchBuilder.BuildTools tools) {
		PanelBatchBuilder.GlobalBuildTools global = tools.global;
		tools.thread.setLevel(global.stats.average);
		EntityId id = tools.global.ed.createEntity();
		tools.global.ed.setComponents(id,
				new GameObject("panel"),
				new Position(tools.location),
				new Color(tools.thread.getColorRGBA()),
				new Size(global.size.x, global.size.y, 0f),
				new Visual(GameModelFactory.PANEL, "game_collision"));
		return id;
	}
	
}
