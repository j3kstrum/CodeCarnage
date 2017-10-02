package interpreter;

import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class BehaviorList extends VBox {

    public ToggleGroup toggleGroup;

    public BehaviorList() {
        super();

        ToggleGroup toggleGroup = new ToggleGroup();

        this.setToggleGroup(toggleGroup);

        setOnMouseClicked(event -> {
            if (event.getTarget() instanceof Behavior) {
                toggleGroup.selectToggle((Behavior) event.getTarget());
            } else {
                toggleGroup.selectToggle(null);
            }
        });

        getStylesheets().add(
                this.getClass().getResource("/styles/script.css").toExternalForm()
        );

    }

    public BehaviorList(Behavior... behaviors) {
        super(10, behaviors);

        ToggleGroup toggleGroup = new ToggleGroup();
        this.setToggleGroup(toggleGroup);
        for (Behavior behavior : behaviors) {
            behavior.setToggleGroup(toggleGroup);
        }

        setOnMouseClicked(event -> {
            if (event.getTarget() instanceof Behavior) {
                toggleGroup.selectToggle((Behavior) event.getTarget());
            } else {
                toggleGroup.selectToggle(null);
            }
        });

        getStylesheets().add(
                this.getClass().getResource("/styles/script.css").toExternalForm()
        );
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup = toggleGroup;
    }
}
