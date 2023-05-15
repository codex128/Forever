/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

/**
 *
 * @author gary
 */
public class StepTag {
	
	private final String tag;
	private final int stepnum;
	
	public StepTag(String tag, int stepnum) {
		this.tag = tag;
		this.stepnum = stepnum;
	}
	
	public String getTag() {
		return tag;
	}
	public int getStepNum() {
		return stepnum;
	}
	
}
