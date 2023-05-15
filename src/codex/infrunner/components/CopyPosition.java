/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.components;

import codex.jmeutil.math.AxisConstraint;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class CopyPosition implements EntityComponent {
	
	private final EntityId copy;
	private final AxisConstraint constraint = new AxisConstraint();
	
	public CopyPosition(EntityId copy) {
		this.copy = copy;
	}
	public CopyPosition(EntityId copy, AxisConstraint constraint) {
		this(copy);
		this.constraint.set(constraint);
	}
	
	public EntityId getCopiedEntity() {
		return copy;
	}
	public AxisConstraint getConstraint() {
		return constraint;
	}
	@Override
	public String toString() {
		return "CopyTranslation["+copy+", "+constraint+"]";
	}
	
}
