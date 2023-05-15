/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.jmeutil.control.GeometryControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author gary
 */
public class VertBumpControl extends GeometryControl {

	String key;
	Vector3f offset = new Vector3f();
	float scalar = .9f;
	
	public VertBumpControl(String key) {
		this.key = key;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		offset.multLocal(scalar);
		geometry.getMaterial().setVector3(key, offset);
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
	public void setOffset(Vector3f offset) {
		this.offset.set(offset);
	}
	
}
