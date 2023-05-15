/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.infrunner;

/**
 *
 * @author gary
 */
public interface DialogListener {
	
	public abstract void onDialogUpdate(DialogAppState state);
	public abstract void onDialogEnd(DialogAppState state);
	
}
