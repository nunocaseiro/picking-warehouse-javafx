package ipleiria.estg.dei.ei.pi.model.experiments;

import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.List;

public class ParameterGUI {
    private String id;
    private List<String> parameters;
    private TextArea textArea;
    private Control control;

    public ParameterGUI(String id, TextArea textArea, Control control,String defaultValue) {
        this.id = id;
        this.parameters = new LinkedList<>();
        this.textArea = textArea;
        this.control = control;
        this.parameters.add(defaultValue);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }
}
