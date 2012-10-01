package edu.mayo.phenoportal.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import edu.mayo.phenoportal.shared.User;

public class UserCredentialsForm extends DynamicForm {
    private static final int WIDTH = 270;
    private static final int HEIGHT = 220;

    protected static final int WIDGET_WIDTH = 150;

    private TextItem firstNameItem;
    private TextItem lastNameItem;
    private TextItem emailItem;
    private TextItem userIdItem;
    private PasswordItem passwordItem;
    private PasswordItem passwordItem2;

    private final int i_widgetWidth;
    private final boolean i_showGroup;

    private final TitleOrientation titleOrientation = TitleOrientation.TOP;

    public UserCredentialsForm(int widgetWidth, boolean showGroup) {
        super();

        i_widgetWidth = widgetWidth;
        i_showGroup = showGroup;
        init();
    }

    public UserCredentialsForm() {
        this(WIDGET_WIDTH, false);
    }

    private void init() {

        setMargin(10);

        setCellPadding(6);
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setTitleOrientation(titleOrientation);
        setAutoFocus(true);
        setNumCols(2);
        setAlign(Alignment.LEFT);

        setIsGroup(i_showGroup);
        setGroupTitle("Set Up Your User ID and Password");

        firstNameItem = new TextItem("firstName");
        firstNameItem.setSelectOnFocus(true);
        firstNameItem.setTitle("First Name");
        firstNameItem.setWidth(i_widgetWidth * 2);
        firstNameItem.setRequired(true);
        firstNameItem.setValidators(FormValidators.getInstance().getNameLengthRangeValidator());
        firstNameItem.setColSpan(2);

        lastNameItem = new TextItem("lastName");
        lastNameItem.setSelectOnFocus(true);
        lastNameItem.setTitle("Last Name");
        lastNameItem.setWidth(i_widgetWidth * 2);
        lastNameItem.setRequired(true);
        lastNameItem.setValidators(FormValidators.getInstance().getNameLengthRangeValidator());
        lastNameItem.setColSpan(2);

        emailItem = new TextItem("email");
        emailItem.setSelectOnFocus(true);
        emailItem.setTitle("Email");
        emailItem.setWidth(i_widgetWidth * 2);
        emailItem.setRequired(true);
        emailItem.setValidators(FormValidators.getInstance().getRegExpValidator());
        emailItem.setColSpan(2);

        userIdItem = new TextItem("userId");
        userIdItem.setSelectOnFocus(true);
        userIdItem.setTitle("User Id");
        userIdItem.setWidth(i_widgetWidth * 2);
        userIdItem.setRequired(true);
        userIdItem.setValidators(FormValidators.getInstance().getUserIdLengthRangeValidator());
        userIdItem.setColSpan(2);

        passwordItem = new PasswordItem("password");
        passwordItem.setTitle("Password");
        passwordItem.setWidth(i_widgetWidth);
        passwordItem.setRequired(true);
        passwordItem.setValidators(FormValidators.getInstance().getPasswordLengthRangeValidator(),
                FormValidators.getInstance().getMatchesValidator());

        passwordItem2 = new PasswordItem("password2");
        passwordItem2.setTitle("Password Confirm");
        passwordItem2.setWidth(i_widgetWidth);
        passwordItem2.setRequired(true);
        passwordItem2.setValidators(FormValidators.getInstance().getPasswordLengthRangeValidator());
        passwordItem2.setWrapTitle(false);

        setFields(new FormItem[] { firstNameItem, lastNameItem, emailItem, userIdItem,
                passwordItem, passwordItem2, });
    }

    public void setIdFieldDisabled(boolean disabled) {
        userIdItem.setDisabled(disabled);

    }

    public void PasswordFieldsSetVisible(boolean visible) {
        passwordItem.setVisible(visible);
        passwordItem2.setVisible(visible);
    }

    public void setData(User user) {
        firstNameItem.setValue(user.getFirstName());
        lastNameItem.setValue(user.getLastName());
        emailItem.setValue(user.getEmail());
        userIdItem.setValue(user.getUserName());
        passwordItem.setValue("");
        passwordItem2.setValue("");
    }
}
