/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner.components;

import com.simsilica.es.EntityComponent;
import java.util.Arrays;

/**
 *
 * @author gary
 */
public class Dialog implements EntityComponent {
	
	private final String[] dialog;
	
	public Dialog(String... dialog) {
		this.dialog = dialog;
	}
	
	public String[] getDialog() {
		return dialog;
	}
	@Override
	public String toString() {
		return "Dialog["+Arrays.toString(dialog)+"]";
	}
	
}
