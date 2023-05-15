/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.control.GeometryControl;
import com.jme3.material.MatParam;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author gary
 */
public class TextureScrollControl extends GeometryControl {
	
	String key;
	Vector2f offset;
	Vector2f speed;
	
	public TextureScrollControl(String key, Vector2f offset, Vector2f speed) {
		this.key = key;
		this.offset = offset;
		this.speed = speed;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		offset.addLocal(speed.mult(tpf));
		geometry.getMaterial().setVector2(key, offset);
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
}
