/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.effects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Image;
import java.util.ArrayList;

/**
 *
 * @author gary
 */
public class MotionBlurFilter extends Filter {
	
	public MotionBlurFilter() {
		super("MotionBlur");
	}
	
    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        postRenderPasses = new ArrayList<>();
        final Material calcMaterial = new Material(manager, "Shaders/MotionBlur.j3md");
        Pass calcPass = new Pass() {
            @Override
            public boolean requiresSceneAsTexture() {
                return true;
            }            
            @Override
            public void beforeRender() {}
        };
        calcPass.init(renderManager.getRenderer(), w, h, Image.Format.RGBA8, Image.Format.Depth, 1, calcMaterial);
        postRenderPasses.add(calcPass);
        
        material = new Material(manager, "Shaders/MotionBlur.j3md");
        material.setTexture("Texture", calcPass.getRenderedTexture());
		material.setFloat("MinimumBlurRadius", .01f);
    }
    @Override
    protected Material getMaterial() {
        return material;
    }
	
}
