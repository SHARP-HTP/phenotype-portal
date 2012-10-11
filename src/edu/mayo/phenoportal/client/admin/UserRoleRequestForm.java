package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;

import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;

public class UserRoleRequestForm extends DynamicForm {

    private static final int WIDGET_WIDTH = 150;

    private static final String TITLE_USER_NAME = "User Name";
    private static final String TITLE_FIRST_NAME = "First Name";
    private static final String TITLE_LAST_NAME = "Last Name";
    private static final String TITLE_EMAIL = "email";
    private static final String TITLE_REQUEST_DATE = "Request Date";

    private final StaticTextItem i_userName;
    private final StaticTextItem i_firstName;
    private final StaticTextItem i_lastName;
    private final StaticTextItem i_email;
    private final StaticTextItem i_requestDate;

    private final TitleOrientation titleOrientation = TitleOrientation.TOP;

    public UserRoleRequestForm() {
        super();

        setMargin(5);

        setCellPadding(6);
        setWidth100();
        setHeight(75);
        setTitleOrientation(titleOrientation);
        // setAutoFocus(true);
        setNumCols(5);
        setAlign(Alignment.LEFT);

        setIsGroup(true);
        setGroupTitle("User Request");

        setAlign(Alignment.LEFT);

        i_userName = new StaticTextItem();
        i_userName.setWidth(WIDGET_WIDTH);
        i_userName.setTitle(TITLE_USER_NAME);
        i_userName.setAlign(Alignment.LEFT);

        i_firstName = new StaticTextItem();
        i_firstName.setWidth(WIDGET_WIDTH);
        i_firstName.setTitle(TITLE_FIRST_NAME);
        i_firstName.setAlign(Alignment.LEFT);

        i_lastName = new StaticTextItem();
        i_lastName.setWidth(WIDGET_WIDTH);
        i_lastName.setTitle(TITLE_LAST_NAME);
        i_lastName.setAlign(Alignment.LEFT);

        i_email = new StaticTextItem();
        i_email.setWidth(WIDGET_WIDTH);
        i_email.setTitle(TITLE_EMAIL);
        i_email.setAlign(Alignment.LEFT);

        i_requestDate = new StaticTextItem();
        i_requestDate.setWidth(WIDGET_WIDTH);
        i_requestDate.setTitle(TITLE_REQUEST_DATE);
        i_requestDate.setAlign(Alignment.LEFT);

        setFields(i_userName, i_firstName, i_lastName, i_email, i_requestDate);
    }

    public void setData(User user, UserRoleRequest userRoleRequest) {
        i_firstName.setValue(user.getFirstName());
        i_lastName.setValue(user.getLastName());
        i_userName.setValue(user.getUserName());
        i_email.setValue(user.getEmail());
        i_requestDate.setValue(userRoleRequest.getRequestDate());
    }

}
