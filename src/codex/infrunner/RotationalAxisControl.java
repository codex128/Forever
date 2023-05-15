/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author gary
 */
public class RotationalAxisControl extends AbstractControl {

	@Override
	protected void controlUpdate(float tpf) {
		spatial.rotate(tpf, 0f, 0f);
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
}
