/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.panels;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author gary
 */
public class PanelBuildManager implements PanelBatchBuilder {
	
	int globalstep = 0;
	int localstep = 0;
	int delay = 0;
	LinkedList<QueuedBuilder> queue = new LinkedList<>();
	PanelBatchBuilder defaultbuilder;
	
	public PanelBuildManager() {}
	public PanelBuildManager(PanelBatchBuilder defaultbuilder) {
		setDefaultPanelBuilder(defaultbuilder);
	}
	
	public void setDefaultPanelBuilder(PanelBatchBuilder builder) {
		defaultbuilder = builder;
	}
	public PanelBatchBuilder getDefaultPanelBuilder() {
		return defaultbuilder;
	}
	
	public void queuePanelBuilder(PanelBatchBuilder builder, Integer remaining) {
		queue.addLast(new QueuedBuilder(builder, remaining));
	}
	public void removeCurrentBuilder() {
		queue.removeFirst();
	}
	public QueuedBuilder getCurrentBuilder() {
		return queue.getFirst();
	}
	public void clearBuilderQueue() {
		queue.clear();
	}
	public Queue<QueuedBuilder> getBuilderQueue() {
		return queue;
	}
	
	public void resetGlobalStep() {
		globalstep = 0;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int getGlobalStep() {
		return globalstep;
	}
	public int getDelay() {
		return delay;
	}
	
	@Override
	public boolean discardPanel(BuildTools tools) {
		return queue.getFirst().builder.discardPanel(tools);
	}
	@Override
	public EntityId createPanelEntity(BuildTools tools) {
		return queue.getFirst().builder.createPanelEntity(tools);
	}
	@Override
	public EntityId[] buildPanelBatch(EntityData ed, Vector3f base, PanelThread[] threads,
			Vector3f size, int step, int levelzero) {
		EntityId[] out;
		if (!queue.isEmpty()) {
			out = build(queue.getFirst().builder, ed, base, threads, size, localstep, levelzero);
			globalstep++;
			localstep++;
			if (queue.getFirst().sum != null && globalstep >= queue.getFirst().sum+delay) {
				queue.removeFirst();
				localstep = 0;
			}
		}
		else if (defaultbuilder != null) {
			out = build(defaultbuilder, ed, base, threads, size, step, levelzero);
		}
		else {
			out = new EntityId[threads.length];
		}
		return out;
	}
	private EntityId[] build(PanelBatchBuilder builder, EntityData ed, Vector3f base,
			PanelThread[] threads, Vector3f size, int step, int levelzero) {
		return builder.buildPanelBatch(ed, base, threads, size, step, levelzero);
	}
	
		
	
	public static class QueuedBuilder {
		private PanelBatchBuilder builder;
		private Integer sum;
		private QueuedBuilder(PanelBatchBuilder builder, Integer remaining) {
			this.builder = builder;
			this.sum = remaining;
		}
		public PanelBatchBuilder getBuilder() {
			return builder;
		}
		public Integer getSum() {
			return sum;
		}
	}
	
}
