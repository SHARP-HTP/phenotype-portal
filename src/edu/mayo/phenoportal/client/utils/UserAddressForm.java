package edu.mayo.phenoportal.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import edu.mayo.phenoportal.shared.User;

/**
 * Form for displaying the user's profile and for letting the user to register.
 */
public class UserAddressForm extends DynamicForm {

    private static final int WIDTH = 295;
    private static final int HEIGHT = 220;

    protected static final int WIDGET_WIDTH = 150;

    private ComboBoxItem i_countryOrRegion;
    private TextItem i_streetAddress;
    private TextItem i_city;
    private ComboBoxItem i_state;
    private TextItem i_zipCode;
    private TextItem i_phoneNumber;

    private final int i_widgetWidth;
    private final boolean i_showGroup;

    private final TitleOrientation titleOrientation = TitleOrientation.TOP;

    public UserAddressForm(int widgetWidth, boolean showGroup) {
        super();

        i_widgetWidth = widgetWidth;
        i_showGroup = showGroup;
        init();
    }

    public UserAddressForm() {
        this(WIDGET_WIDTH, false);
    }

    private void init() {
        setMargin(5);

        setCellPadding(6);
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setTitleOrientation(titleOrientation);
        // setAutoFocus(true);
        setNumCols(2);
        setAlign(Alignment.LEFT);

        setIsGroup(i_showGroup);
        setGroupTitle("Tell Us About Yourself");

        i_countryOrRegion = new ComboBoxItem("countryOrRegion");
        i_countryOrRegion.setTitle("Country / Region");
        i_countryOrRegion.setWidth(i_widgetWidth * 2);
        i_countryOrRegion.setRequired(true);
        i_countryOrRegion.setValueMap("United States", "China", "Japan", "India");
        i_countryOrRegion.setColSpan(2);

        i_streetAddress = new TextItem("street");
        i_streetAddress.setSelectOnFocus(true);
        i_streetAddress.setTitle("Street Address");
        i_streetAddress.setWidth(i_widgetWidth * 2);
        i_streetAddress.setRequired(true);
        i_streetAddress.setColSpan(2);

        i_city = new TextItem("city");
        i_city.setSelectOnFocus(true);
        i_city.setTitle("City");
        i_city.setWidth(i_widgetWidth);
        i_city.setRequired(true);

        i_state = new ComboBoxItem("state");
        i_state.setSelectOnFocus(true);
        i_state.setTitle("State");
        i_state.setWidth(i_widgetWidth);
        i_state.setValueMap("Arizona", "Florida", "Minnesota", "Wisconsin");
        i_state.setRequired(true);

        i_zipCode = new TextItem("zip");
        i_zipCode.setSelectOnFocus(true);
        i_zipCode.setTitle("Zip / Postal Code");
        i_zipCode.setWidth(i_widgetWidth * 2);
        i_zipCode.setRequired(true);
        i_zipCode.setColSpan(2);

        i_phoneNumber = new TextItem("phone");
        i_phoneNumber.setMask("(###) ###-####");
        i_phoneNumber.setSelectOnFocus(true);
        i_phoneNumber.setTitle("Phone Number");
        i_phoneNumber.setWidth(i_widgetWidth * 2);
        i_phoneNumber.setRequired(true);
        i_phoneNumber.setWrapTitle(false);
        i_phoneNumber.setColSpan(2);

        setFields(new FormItem[] { i_countryOrRegion, i_streetAddress, i_city, i_state, i_zipCode,
                i_phoneNumber });
    }

    public void setData(User user) {
        i_countryOrRegion.setValue(user.getCountryRegion());
        i_streetAddress.setValue(user.getAddress());
        i_city.setValue(user.getCity());
        i_state.setValue(user.getState());
        i_zipCode.setValue(user.getZipCode());
        i_phoneNumber.setValue(user.getPhoneNumber());
    }

    /**
     * Minimize the amount of space this form will take up vertically.
     */
    public void setMinimumHeight() {
        i_zipCode.setColSpan(1);
        i_zipCode.setWidth(i_widgetWidth);

        i_phoneNumber.setColSpan(1);
        i_phoneNumber.setWidth(i_widgetWidth);

    }

}
