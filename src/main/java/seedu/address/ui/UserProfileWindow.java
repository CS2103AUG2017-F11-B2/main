package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.UserPersonChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UserPerson;

/**
 * Controller for the User Profile Window
 */
public class UserProfileWindow extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "UserProfileWindow.fxml";
    private static final String TITLE = "User Profile";

    private UserPerson userPerson;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    private final Stage stage;

    public UserProfileWindow(UserPerson userPerson) {
        super(FXML);
        registerAsAnEventHandler(this);

        Scene scene = new Scene(getRoot());
        stage = createDialogStage(TITLE, null, scene);
        statusLabel.setText("");
        this.userPerson = userPerson;
        nameTextField.setText(userPerson.getName().toString());
        emailTextField.setText(userPerson.getEmailAsText());
        phoneTextField.setText(userPerson.getPhone().toString());
        addressTextField.setText(userPerson.getAddress().toString());

        setAccelerators(scene);

    }

    /**
     * Sets accelerators for the UserProfileWindow
     * @param scene
     */
    private void setAccelerators(Scene scene) {
        scene.getAccelerators().put(KeyCombination.valueOf("ENTER"), ()-> handleCancel());
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(UserPerson userPerson) {
        nameTextField.textProperty().bind(Bindings.convert(userPerson.nameProperty()));
        phoneTextField.textProperty().bind(Bindings.convert(userPerson.phoneProperty()));
        addressTextField.textProperty().bind(Bindings.convert(userPerson.addressProperty()));
        emailTextField.textProperty().bind(Bindings.convert(userPerson.emailProperty()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DetailedPersonCard)) {
            return false;
        }

        // state check
        UserProfileWindow otherUserProfileWindow = (UserProfileWindow) other;
        return userPerson.equals(otherUserProfileWindow.userPerson);
    }

    /**
     *
     */
    public void show() {
        logger.fine("Showing User Profile.");
        stage.showAndWait();
    }

    @FXML
    void handleCancel() {
        stage.close();
    }

    /**
     * Handles the OK button
     */
    @FXML
    void handleOk() {
        try {
            updateUserPerson();
            raise(new UserPersonChangedEvent(userPerson));
            stage.close();
        } catch (Exception e) {
            logger.fine("Invalid UserPerson modification");
        }
    }

    @Subscribe
    private void handleUserPersonChangedEvent(UserPersonChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        //bindListeners(userPerson);
    }

    /**
     * Updates the user person
     */
    void updateUserPerson() throws Exception {
        try {
            userPerson.setName(new Name(nameTextField.getText()));
        } catch (IllegalValueException e) {
            statusLabel.setText("Illegal Name value, only alphanumeric values accepted");
            throw new Exception();
        }

        try {
            userPerson.setPhone(new Phone(phoneTextField.getText()));
        } catch (IllegalValueException e) {
            statusLabel.setText("Illegal Phone number, only numeric values accepted");
            throw new Exception();
        }

        try {
            String[] emails = emailTextField.getText().split(", ");
            ArrayList<Email> emailList = new ArrayList<>();
            for (String curr : emails) {
                emailList.add(new Email(curr));
            }
            userPerson.setEmail(emailList);

        } catch (IllegalValueException e) {
            statusLabel.setText("Email(s) must be in x@x format");
            throw new Exception();
        }

        try {
            userPerson.setAddress(new Address(addressTextField.getText()));
        } catch (IllegalValueException e) {
            statusLabel.setText("Please input a valid address value");
            throw new Exception();
        }
    }
}