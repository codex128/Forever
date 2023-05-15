/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.awt.Color;

/**
 *
 * @author gary
 */
public class GameUtils {
	
	public static Quaternion rotateFromAxisX(float angle) {
		return new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_X);
	}
	public static Quaternion rotateFromAxisY(float angle) {
		return new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y);
	}
	public static Quaternion rotateFromAxisZ(float angle) {
		return new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Z);
	}
	
	public static ColorRGBA HSBAtoRGBA(float hue, float saturation, float brightness, int alpha) {
		Color hsb = Color.getHSBColor(hue, saturation, brightness);
		return ColorRGBA.fromRGBA255(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
	}
	public static ColorRGBA randomHue(float saturation, float brightness, int alpha) {
		Color hsb = Color.getHSBColor(FastMath.rand.nextFloat(), saturation, brightness);
		return ColorRGBA.fromRGBA255(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), alpha);
	}
	
	public static int wrap(int value, int min, int max) {
		if (value > max) value = min+(value-max-1);
		else if (value < min) value = max-(min-value)+1;
		return value;
	}
	public static float random(float a, float b) {
		return a+FastMath.rand.nextFloat()*(b-a);
	}
	
}
