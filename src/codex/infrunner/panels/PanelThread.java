/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.SceneGeneratorState;
import codex.jmeutil.ColorHSBA;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class PanelThread {
	
	private static float hue = 0f;
	
	Vector2f location;
	int level = 0;
	int tendency = 0;
	ColorHSBA color = new ColorHSBA((hue = wrap(hue+.1f, 0f, 1f)), .75f, 1f, 1f);
	
	public PanelThread(Vector2f location) {
		this.location = location;
	}
	public PanelThread(Vector2f location, int level) {
		this.location = location;
		this.level = level;
	}
	
	public void setLocation(Vector2f location) {
		this.location.set(location);
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setUpTendency(int tendency) {
		this.tendency = tendency;
	}
	
	public Vector3f get3DLocation() {
		return new Vector3f(location.x, level*SceneGeneratorState.PANEL_SIZE.z, location.y);
	}
	public Vector2f get2DLocation() {
		return location;
	}
	public int getLevel() {
		return level;
	}
	public int getUpTendency() {
		return tendency;
	}
	public ColorRGBA getColorRGBA() {
		return color.toRGBA();
	}
	public ColorHSBA getColor() {
		return color;
	}
	
	private static float wrap(float value, float min, float max) {
		if (value > max) return value-(max-min);
		else if (value < min) return value+(max-min);
		else return value;
	}
	
}
