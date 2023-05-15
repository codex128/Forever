/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.infrunner.panels;

import codex.infrunner.SceneGeneratorState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public interface PanelBatchBuilder {	
	
	public abstract boolean discardPanel(BuildTools tools);
	public abstract EntityId createPanelEntity(BuildTools tools);
	
	public default EntityId[] buildPanelBatch(EntityData ed, Vector3f base,
			PanelThread[] threads, Vector3f size, int step, int levelzero) {
		GlobalBuildTools global = new GlobalBuildTools(
				ed, threads, new ThreadGroupStatistics(threads), size, step, levelzero);
		return buildPanelBatch(global, base);
	}
	public default EntityId[] buildPanelBatch(GlobalBuildTools global, Vector3f base) {
		initializeBatch(global, base);
		EntityId[] entities = new EntityId[global.threads.length];
		int index = -1;
		for (PanelThread thread : global.threads) {
			index++;
			BuildTools tools = new BuildTools(global, thread, index,
					base.add(thread.get3DLocation()).addLocal(0f, global.levelZero*SceneGeneratorState.PANEL_SIZE.z, 0f));
			if (discardPanel(tools)) continue;
			entities[index] = createPanelEntity(tools);
		}
		return entities;
	}
	public default void initializeBatch(GlobalBuildTools global, Vector3f base) {}
	
	
	public static class GlobalBuildTools {
		public final EntityData ed;
		public final PanelThread[] threads;
		public final ThreadGroupStatistics stats;
		public final Vector3f size;
		public final int step;
		public final int levelZero;
		public GlobalBuildTools(EntityData ed, PanelThread[] threads, ThreadGroupStatistics stats,
				Vector3f size, int step, int levelZero) {
			this.ed = ed;
			this.threads = threads;
			this.stats = stats;
			this.size = size;
			this.step = step;
			this.levelZero = levelZero;
		}	
	}
	public static class BuildTools {
		public final GlobalBuildTools global;
		public final PanelThread thread;
		public final int index;
		public final Vector3f location;
		public BuildTools(GlobalBuildTools global, PanelThread thread, int index,
				Vector3f location) {
			this.global = global;
			this.thread = thread;
			this.index = index;
			this.location = location;
		}
	}
	public static class ThreadGroupStatistics {
		public PanelThread highest;
		public PanelThread lowest;
		public int average;
		public ThreadGroupStatistics(PanelThread highest, PanelThread lowest, int average) {
			this.highest = highest;
			this.lowest = lowest;
			this.average = average;
		}
		public ThreadGroupStatistics(PanelThread[] threads) {
			int total = 0;
			for (PanelThread t : threads) {
				if (highest == null || highest.getLevel() < t.getLevel()) {
					highest = t;
				}
				if (lowest == null || lowest.getLevel() > t.getLevel()) {
					lowest = t;
				}
				total += t.getLevel();
			}
			average = total/threads.length;
		}
	}
	
}
