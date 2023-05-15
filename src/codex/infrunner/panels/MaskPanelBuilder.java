/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.SceneGeneratorState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class MaskPanelBuilder implements PanelBatchBuilder {

	PanelBatchBuilder mask;
	PanelBatchBuilder builder;
	
	public MaskPanelBuilder(PanelBatchBuilder mask, PanelBatchBuilder builder) {
		this.mask = mask;
		this.builder = builder;
	}
	
	@Override
	public boolean discardPanel(BuildTools tools) {
		return false;
	}
	@Override
	public EntityId createPanelEntity(BuildTools tools) {
		return null;
	}
	@Override
	public EntityId[] buildPanelBatch(GlobalBuildTools global, Vector3f base) {
		mask.initializeBatch(global, base);
		builder.initializeBatch(global, base);
		EntityId[] entities = new EntityId[global.threads.length];
		int index = -1;
		for (PanelThread thread : global.threads) {
			index++;
			BuildTools tools = new BuildTools(global, thread, index,
					base.add(thread.get3DLocation()).addLocal(0f, global.levelZero*SceneGeneratorState.PANEL_SIZE.z, 0f));
			if (mask.discardPanel(tools) ||
					builder.discardPanel(tools)) continue;
			entities[index] = builder.createPanelEntity(tools);
		}
		return entities;
	}
	
}
