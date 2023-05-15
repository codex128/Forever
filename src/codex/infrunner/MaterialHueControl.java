/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.control.GeometryControl;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.awt.Color;

/**
 *
 * @author gary
 */
public class MaterialHueControl extends GeometryControl {
	
	String param;
	float hue;
	float speed;
	float saturation;
	float brightness;
	int alpha;
	
	public MaterialHueControl(String param, float hue, float speed, float saturation, float brightness, int alpha) {
		this.param = param;
		this.hue = hue;
		this.speed = speed;
		this.saturation = saturation;
		this.brightness = brightness;
		this.alpha = alpha;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		hue += speed;
		if (hue > 1f) hue--;
		else if (hue < 0f) hue++;
		geometry.getMaterial().setColor(param, convert());
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
	private ColorRGBA convert() {
		Color hsb = Color.getHSBColor(hue, saturation, brightness);
		return ColorRGBA.fromRGBA255(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
	}
	
}
