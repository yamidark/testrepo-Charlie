package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskStateChangeEvent;
import seedu.address.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;

/**
 * The Browser Panel of the App.
 */
public class ViewTaskPanel extends UiPart<Region> {

    private static final String FXML = "ViewTaskPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Label taskName;
    @FXML
    private Label description;
    @FXML
    private Label deadline;
    @FXML
    private Label priority;
    @FXML
    private Label state;
    @FXML
    private Button markState;

    private ReadOnlyTask task;

    public ViewTaskPanel(ReadOnlyTask task) {
        super(FXML);
        if (task != null) {
            this.task = task;
            initializeWithTask(task);
        }
        registerAsAnEventHandler(this);
    }

    public ViewTaskPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Task} properties
     * so that they will be notified of any changes.
     */
    private void initializeWithTask(ReadOnlyTask task) {
        taskName.textProperty().bind(Bindings.convert(task.taskNameProperty()));
        description.textProperty().bind(Bindings.convert(task.descriptionProperty()));
        deadline.textProperty().bind(Bindings.convert(task.deadlineProperty()));
        priority.textProperty().bind(Bindings.convert(task.priorityProperty()));
        state.textProperty().bind(Bindings.convert(task.stateProperty()));
        markState.textProperty().bind(Bindings.convert(task.changeStateProperty()));
        markState.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ReadOnlyTask updatedTask = new Task(task);
                updatedTask.changeState();
                raise(new TaskStateChangeEvent(task, updatedTask));
            }
        });
    }

    @Subscribe
    private void handleTaskPanelSelectionChangedEvent(TaskPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        initializeWithTask(event.getNewSelection().task);
    }
}
