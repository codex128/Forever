/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.infrunner;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

/**
 *
 * @author gary
 */
public class Functions {
	
	public static final String
			RUNNER_GROUP = "runner_group",
			GAME_GROUP = "game_group";
	
	public static final FunctionId
			F_STRAFE = new FunctionId(RUNNER_GROUP, "strafe"),
			F_JUMP = new FunctionId(RUNNER_GROUP, "jump"),
			F_DEV_FLOAT = new FunctionId(RUNNER_GROUP, "dev_float"),
			F_DEV_STEP = new FunctionId(GAME_GROUP, "dev_step");
	
	public static void initialize(InputMapper im) {
		im.map(F_STRAFE, InputState.Positive, KeyInput.KEY_D);
		im.map(F_STRAFE, InputState.Positive, KeyInput.KEY_RIGHT);
		im.map(F_STRAFE, InputState.Negative, KeyInput.KEY_A);
		im.map(F_STRAFE, InputState.Negative, KeyInput.KEY_LEFT);
		im.map(F_STRAFE, Axis.JOYSTICK_LEFT_X);
		im.map(F_STRAFE, Axis.JOYSTICK_HAT_X);
		im.map(F_JUMP, KeyInput.KEY_SPACE);
		im.map(F_JUMP, Button.JOYSTICK_BUTTON1);
		im.map(F_DEV_FLOAT, KeyInput.KEY_UP);
		im.map(F_DEV_STEP, KeyInput.KEY_S);
	}
	
}
