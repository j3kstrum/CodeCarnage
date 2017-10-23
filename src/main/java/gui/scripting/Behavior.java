package gui.scripting;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;

public class Behavior extends FlowPane implements Toggle {

    /**
     * PseudoClass that refers to the selected css class
     */
    private static final PseudoClass
            SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    /**
     * Sets up an association between the selected property and the Behavior's underlying css class
     */
    private BooleanProperty selected =
            new BooleanPropertyBase(false) {

                /**
                 * Changes the underlying pseudoClass of the current Behavior object, based on the value of selected
                 */
                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
                }

                /**
                 * @return the Behavior Object that contains the selected property
                 */
                @Override
                public Object getBean() {
                    return Behavior.this;
                }

                /**
                 * @return the name of the PseudoClass that corresponds with the BooleanProperty
                 */
                @Override
                public String getName() {
                    return "selected";
                }
            };

    private ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>();

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
}
