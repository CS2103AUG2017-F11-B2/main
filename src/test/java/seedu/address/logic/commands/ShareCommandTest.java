package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalUserPerson.WILLIAM;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.UserPerson;
import seedu.address.model.util.SampleUserPersonUtil;

//@@author bladerail
public class ShareCommandTest {

    private Model model;
    private ShareCommand shareCommand;

    @Test
    public void execute_showsAddCommandCorrectly() {
        model = new ModelManager(new AddressBook(), new UserPrefs(), new UserPerson());

        shareCommand = new ShareCommand();
        shareCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        String expectedResult = String.format(ShareCommand.MESSAGE_SUCCESS,
                ShareCommand.addCommandBuilder(
                SampleUserPersonUtil.getDefaultSamplePerson()));
        assertCommandSuccess(shareCommand, model, expectedResult, model);
    }

    @Test
    public void execute_addsCorrectUserPerson() {
        model = new ModelManager(new AddressBook(), new UserPrefs(), new UserPerson());
        model.updateUserPerson(WILLIAM);

        shareCommand = new ShareCommand();
        shareCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        String addCommandWilliam = ShareCommand.addCommandBuilder(WILLIAM);
        String expectedResult = String.format(ShareCommand.MESSAGE_SUCCESS, addCommandWilliam);
        assertCommandSuccess(shareCommand, model, expectedResult, model);
    }
}
