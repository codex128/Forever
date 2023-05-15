/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import codex.infrunner.components.Dialog;
import codex.j3map.processors.J3mapPropertyProcessor;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class DialogProcessor implements J3mapPropertyProcessor<Dialog> {

	@Override
	public Class<Dialog> type() {
		return Dialog.class;
	}
	@Override
	public Dialog process(String str) {
		if (!str.startsWith(getPropertyIdentifier()+"(") || !str.endsWith(")")) {
			return null;
		}
		LinkedList<String> args = new LinkedList<>();
		boolean inString = false;
		String arg = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '"') {
				inString = !inString;
			}
			else if (inString) {
				arg += c;
			}
			else if (c == ',' && !arg.isEmpty()) {
				args.add(""+arg);
				arg = "";
			}
		}
		if (!arg.isEmpty()) {
			args.add(arg);
		}
		String[] array = new String[args.size()];
		int index = 0;
		for (String a : args) {
			array[index++] = a;
		}
		return new Dialog(array);
	}
	@Override
	public String[] export(Dialog property) {
		String args = "";
		for (String d : property.getDialog()) {
			args += "\""+d+"\",";
		}
		return new String[]{getPropertyIdentifier()+"("+args+")"};
	}
	@Override
	public String getPropertyIdentifier() {
		return "Dialog";
	}
	@Override
	public Dialog[] createArray(int length) {
		return new Dialog[length];
	}
	
}
