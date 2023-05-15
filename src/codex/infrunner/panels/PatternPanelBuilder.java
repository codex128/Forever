/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.GameModelFactory;
import codex.infrunner.GameUtils;
import codex.jmeutil.es.components.Color;
import codex.jmeutil.es.components.GameObject;
import codex.jmeutil.es.components.Position;
import codex.jmeutil.es.components.Size;
import codex.jmeutil.es.components.Visual;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class PatternPanelBuilder implements PanelBatchBuilder {
	
	
	int median;
	int step = 0;
	Integer height = 0;
	long console = 0l;
	int[] values;
	final int strands = 4;
	ColorRGBA[] colors = new ColorRGBA[strands];
	ColorRGBA currentColor;
	
	public PatternPanelBuilder() {
		this(0);
	}
	public PatternPanelBuilder(Integer height) {
		this.height = height;
		initializeColors();
	}
	
	private void initializeColors() {
		for (int i = 0; i < colors.length; i++) {
			colors[i] = GameUtils.randomHue(1f, 1f, 255);
		}
		currentColor = colors[0];
	}
	
	@Override
	public boolean discardPanel(BuildTools tools) {
		if (height == null) {
			height = tools.global.stats.average;
		}
		int index = 0;
		currentColor = null;
		for (int v : values) {
			if (v == tools.index
					|| v-1 == tools.index
					|| v+1 == tools.index) {
				if (currentColor == null || FastMath.rand.nextFloat() > .5f) {
					currentColor = colors[index];
				}
			}
			index++;
		}
		if (currentColor != null) {
			return false;
		}
		return true;
	}
	@Override
	public EntityId createPanelEntity(BuildTools tools) {
		PanelBatchBuilder.GlobalBuildTools global = tools.global;
		if (tools.thread.getLevel() != height) {
			tools.thread.setLevel(height);
		}
		EntityId id = global.ed.createEntity();		
		global.ed.setComponents(id,
			new GameObject("panel"),
			new Position(tools.location),
			new Size(global.size.x, global.size.y, 0f),
			new Visual(GameModelFactory.PANEL, "game_collision"),
			new Color(tools.thread.getColorRGBA()));
		return id;
	}
	@Override
	public void initializeBatch(GlobalBuildTools global, Vector3f base) {
		PanelThread[] threads = global.threads;
		step++;
		console++;
		median = threads.length/2;
		values = new int[strands];
		values[0] = (int)(FastMath.cos(((float)step/8f)+FastMath.PI)*threads.length/2)+median;
		values[1] = (int)(FastMath.cos((float)step/8f)*threads.length/2)+median;
		values[2] = (int)(FastMath.sin(((float)step/8f)+FastMath.PI)*threads.length/2)+median;
		values[3] = (int)(FastMath.sin((float)step/8f)*threads.length/2)+median;
	}

	
}
