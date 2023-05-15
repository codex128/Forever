/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author gary
 */
public class TransformAnimControl extends AbstractControl {
	
	Vector3f translate = new Vector3f();
	Quaternion rotate = Quaternion.ZERO.clone();
	Vector3f scale = new Vector3f();
	
	public TransformAnimControl() {}
	
	public TransformAnimControl setTranslation(Vector3f translate) {
		this.translate.set(translate);
		return this;
	}
	public TransformAnimControl setRotation(Quaternion rotate) {
		this.rotate.set(rotate);
		return this;
	}
	public TransformAnimControl setScale(Vector3f scale) {
		this.scale.set(scale);
		return this;
	}
	public TransformAnimControl setScale(float scalar) {
		scale.set(scalar, scalar, scalar);
		return this;
	}

	@Override
	protected void controlUpdate(float tpf) {
		spatial.move(translate.mult(tpf));
		spatial.rotate(spatial.getLocalRotation().add(rotate.mult(tpf)));
		Vector3f s = scale;
		spatial.scale(s.x, s.y, s.z);
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}	
	
}
