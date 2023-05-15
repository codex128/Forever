/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Dialog;
import codex.jmeutil.es.ESAppState;
import codex.jmeutil.es.VisualState;
import codex.jmeutil.listen.Listenable;
import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class DialogAppState extends ESAppState implements Listenable<DialogListener> {
	
	Dialog dialog;
	int index = 0;
	BitmapText text;
	LinkedList<DialogListener> listeners = new LinkedList<>();
	
	public DialogAppState(Dialog dialog) {
		this.dialog = dialog;
	}
	
	@Override
	protected void init(Application app) {
		visuals.registerScene("dialog_gui", new Node("dialog"), VisualState.GUINODE);		
        BitmapFont font = assetManager.loadFont("Interface/Fonts/LiberationSansNarrow.fnt");
        text = new BitmapText(font);
        text.setSize(font.getCharSet().getRenderedSize());
        text.setText(dialog.getDialog()[0]);
        text.setLocalTranslation(300, text.getLineHeight()+300, 0);
		visuals.getScene("dialog_gui").attachChild(text);
	}
	@Override
	protected void cleanup(Application app) {
		visuals.removeScene("dialog_gui");
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	
	public void advance(int n) {
		setIndex(index+n);
	}
	public void setIndex(int i) {
		index = Math.max(i, 0);
		if (index >= dialog.getDialog().length) {
			notifyListeners(l -> l.onDialogEnd(this));
		}
		else {
			text.setText(dialog.getDialog()[index]);
			notifyListeners(l -> l.onDialogUpdate(this));
		}		
	}
	
	public Dialog getDialogComponent() {
		return dialog;
	}
	public String[] getDialog() {
		return dialog.getDialog();
	}
	public int getIndex() {
		return index;
	}
	public BitmapText getText() {
		return text;
	}

	@Override
	public Collection<DialogListener> getListeners() {
		return listeners;
	}
	
}
