package interpreter;

import gui.scripting.ScriptButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class Behavior extends FlowPane implements Toggle {
    private static final PseudoClass
            SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    public BooleanProperty selected =
            new BooleanPropertyBase(false) {
                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
                }

                @Override
                public Object getBean() {
                    return Behavior.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }
            };
    private ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>();
    private ArrayList<ScriptButton> blankButtons = new ArrayList<>();

    public Behavior() {
        super();
    }

    public boolean isSelected() {
        return selected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    @Override
    public ToggleGroup getToggleGroup() {
        return toggleGroup.get();
    }

    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup.set(toggleGroup);
    }

    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return toggleGroup;
    }

    public ArrayList<ScriptButton> getBlankButtons() {
        return blankButtons;
    }

    public void setBlankButtons(ArrayList<ScriptButton> blankButtons) {
        this.blankButtons = blankButtons;
    }
}
