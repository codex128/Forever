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
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class ChordPanelBuilder implements PanelBatchBuilder {
	
	int[] chords;
	int index = 0;
	int step = 0;
	int direction = 0;
	
	public ChordPanelBuilder(int... chords) {
		this.chords = chords;
	}

	@Override
	public boolean discardPanel(BuildTools tools) {
		return false;
	}
	@Override
	public EntityId createPanelEntity(BuildTools tools) {
		PanelBatchBuilder.GlobalBuildTools global = tools.global;
		EntityId id = global.ed.createEntity();
		if (direction == 0) {
			global.ed.setComponents(id,
				new GameObject("panel"),
				new Position(tools.location),
				new Size(global.size.x, global.size.y, 0f),
				new Visual(GameModelFactory.PANEL, "game_collision"),
				new Color(tools.thread.getColorRGBA()));
		}
		else {
			if (direction < 0) {
				//tools.thread.setLevel(tools.thread.getLevel()-1);
				global.ed.setComponent(id, new Size(global.size.x, global.size.y, -global.size.z));				
			}
			else {
				//tools.thread.setLevel(tools.thread.getLevel()+1);
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
	@Override
	public void initializeBatch(GlobalBuildTools global, Vector3f base) {
		if (direction != 0) for (PanelThread thread : global.threads) {
			thread.setLevel(thread.getLevel()+direction);
		}
		if (index < chords.length-1 && step >= chords[index]) {
			direction = FastMath.sign(chords[++index]);
			step = 0;
			index++;
		}
		else {
			direction = 0;
			step++;
		}
	}
	
}
