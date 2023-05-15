/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.infrunner;

/**
 *
 * @author gary
 */
public interface SceneGeneratorListener {
	
	public default void onGenerationStep(int step) {}
	public default void onTaggedStep(StepTag tag) {}
	public default void onRunnerDeath(RunnerAppState runner) {}
	
}
