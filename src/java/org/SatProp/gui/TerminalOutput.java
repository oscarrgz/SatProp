package org.SatProp.gui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class TerminalOutput  extends OutputStream{
	private TextArea output;

    public TerminalOutput(TextArea ta) {
        this.output = ta;
    }

    @Override
    public void write(int i) throws IOException {
    	// update ProgressIndicator on FX thread
        Platform.runLater(new Runnable() {
        	public void run() {
        		output.appendText(String.valueOf((char) i));
        	}
        	
        });
//        output.appendText(String.valueOf((char) i));
    }
}

