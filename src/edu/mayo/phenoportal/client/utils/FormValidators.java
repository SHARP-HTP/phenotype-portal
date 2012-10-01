package edu.mayo.phenoportal.client.utils;

import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 * Common class for form validators.
 */
public class FormValidators {

    private static FormValidators instance = null;

    private static LengthRangeValidator i_passwordLengthRangeValidator;
    private static LengthRangeValidator i_nameLengthRangeValidator;
    private static LengthRangeValidator i_userIdLengthRangeValidator;
    private static RegExpValidator i_regExpValidator;
    private static MatchesFieldValidator i_matchesValidator;

    public static FormValidators getInstance() {
        if (instance == null) {
            instance = new FormValidators();
        }
        return instance;
    }

    private FormValidators() {
        super();

        // Validator for pw length
        i_passwordLengthRangeValidator = new LengthRangeValidator();
        i_passwordLengthRangeValidator.setMin(4);
        i_passwordLengthRangeValidator
                .setErrorMessage("Password must be at least 4 characters long");

        // Validator for name length
        i_nameLengthRangeValidator = new LengthRangeValidator();
        i_nameLengthRangeValidator.setMin(1);
        i_nameLengthRangeValidator.setErrorMessage("Name cannot be empty");

        // Validator for user id length
        i_userIdLengthRangeValidator = new LengthRangeValidator();
        i_userIdLengthRangeValidator.setMin(4);
        i_userIdLengthRangeValidator.setErrorMessage("User Id must be at least 4 characters long");

        // Validator for valid email
        i_regExpValidator = new RegExpValidator();
        i_regExpValidator
                .setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
        i_regExpValidator.setErrorMessage("Invalid email address");

        // Validator for passwords to match
        i_matchesValidator = new MatchesFieldValidator();
        i_matchesValidator.setOtherField("password2");
        i_matchesValidator.setErrorMessage("Passwords do not match");
    }

    /**
     * @return the i_passwordLengthRangeValidator
     */
    public LengthRangeValidator getPasswordLengthRangeValidator() {
        return i_passwordLengthRangeValidator;
    }

    /**
     * @return the i_nameLengthRangeValidator
     */
    public LengthRangeValidator getNameLengthRangeValidator() {
        return i_nameLengthRangeValidator;
    }

    /**
     * @return the i_userIdLengthRangeValidator
     */
    public LengthRangeValidator getUserIdLengthRangeValidator() {
        return i_userIdLengthRangeValidator;
    }

    /**
     * @return the i_regExpValidator
     */
    public RegExpValidator getRegExpValidator() {
        return i_regExpValidator;
    }

    /**
     * @return the i_matchesValidator
     */
    public MatchesFieldValidator getMatchesValidator() {
        return i_matchesValidator;
    }

}
