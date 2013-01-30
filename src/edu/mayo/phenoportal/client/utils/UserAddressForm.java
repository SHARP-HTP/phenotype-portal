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
        i_countryOrRegion
                .setValueMap("UNITED STATES", "AFGHANISTAN", "ALBANIA", "ALGERIA",
                        "AMERICAN SAMOA", "ANDORRA", "ANGOLA", "ANGUILLA", "ANTARCTICA",
                        "ANTIGUA AND BARBUDA", "ARGENTINA", "ARMENIA", "ARUBA",
                        "ASHMORE AND CARTIER ISL", "AUSTRALIA", "AUSTRIA", "AZERBAIJAN", "BAHAMAS",
                        " THE", "BAHRAIN", "BAKER ISLAND", "BANGLADESH", "BARBADOS",
                        "BASSAS DA INDIA", "BELARUS", "BELGIUM", "BELIZE", "BENIN", "BERMUDA",
                        "BHUTAN", "BOLIVIA", "BOSNIA AND HERCEGOVINA", "BOTSWANA", "BOUVET ISLAND",
                        "BRAZIL", "BR INDIAN OCEAN TERR", "BRUNEI", "BULGARIA",
                        "BURKINA (UPPER VOLTA)", "BURMA", "BURUNDI", "CAMBODIA", "CAMEROON",
                        "CANADA", "CAPE VERDE", "CAYMAN ISLANDS", "CENTRAL AFRICAN REP.", "CHAD",
                        "CHILE", "CHINA", "CHRISTMAS ISLAND", "CLIPPERTON ISLAND",
                        "COCOS (KEELING) ISLANDS", "COLOMBIA", "COMOROS", "CONGO", "COOK ISLANDS",
                        "CORAL SEA ISLANDS", "COSTA RICA", "IVORY COAST", "CROATIA", "CUBA",
                        "CYPRUS", "CZECH REPUBLIC", "CZECHOSLOVAKIA", "DENMARK", "DJIBOUTI",
                        "DOMINICA", "DOMINICAN REPUBLIC", "PORTUGUESE TIMOR", "ECUADOR", "EGYPT",
                        "EL SALVADOR", "EQUATORIAL GUINEA", "ERITREA", "ESTONIA", "ETHIOPIA",
                        "EUROPA ISLAND", "FALKLAND (IS MALVINAS)", "FAROE ISLANDS", "FIJI",
                        "FINLAND", "FRANCE", "FRENCH GUIANA", "FRENCH POLYNESIA",
                        "FR SO & ANTARCTIC LNDS", "GABON", "GAMBIA", " THE", "GAZA STRIP",
                        "GEORGIA", "GERMANY", "GHANA", "GIBRALTAR", "GLORIOSO ISLANDS", "GREECE",
                        "GREENLAND", "GRENADA", "GUADELOUPE", "GUAM", "GUATEMALA", "GUERNSEY",
                        "GUINEA", "GUINEA-BISSAU", "GUYANA", "HAITI", "HEARD IS&MCDONALD ISLS",
                        "VATICAN CITY", "HONDURAS", "HONG KONG", "HOWLAND ISLAND", "HUNGARY",
                        "ICELAND", "INDIA", "INDONESIA", "IRAN", "IRAQ", "IRELAND", "MAN",
                        " ISLE OF", "ISRAEL", "ITALY", "JAMAICA", "JAN MAYEN", "JAPAN",
                        "JARVIS ISLAND", "JERSEY", "JOHNSTON ATOLL", "JORDAN",
                        "JUAN DE NOVA ISLAND", "KAZAKHSTAN", "KENYA", "KINGMAN REEF", "KIRIBATI",
                        "KOREA", "DEM PEOPLES REP", "KOREA", " REPUBLIC OF", "KUWAIT",
                        "KYRGYZSTAN", "LAOS", "LATVIA", "LEBANON", "LESOTHO", "LIBERIA", "LIBYA",
                        "LIECHTENSTEIN", "LITHUANIA", "LUXEMBOURG", "MACAU", "MACEDONIA",
                        "MADAGASCAR", "MALAWI", "MALAYSIA", "MALDIVES", "MALI", "MALTA",
                        "MARSHALL ISLANDS", "MARTINIQUE", "MAURITANIA", "MAURITIUS", "MAYOTTE",
                        "MEXICO", "FED STATES MICRONESIA", "MIDWAY ISLAND", "MOLDOVA", "MONACO",
                        "MONGOLIA", "MONTENEGRO", "MONTSERRAT", "MOROCCO", "MOZAMBIQUE", "NAMIBIA",
                        "NAURU", "NAVASSA ISLAND", "NEPAL", "NETHERLANDS", "NETHERLANDS ANTILLES",
                        "IRAQ-S ARABIA NEUTRAL Z", "NEW CALEDONIA", "NEW ZEALAND", "NICARAGUA",
                        "NIGER", "NIGERIA", "NIUE", "NOT SPECIFIED", "NORFOLK ISLAND",
                        "NORTHERN MARIANA IS", "NORWAY", "OMAN", "PAKISTAN", "PALAU",
                        "PALMYRA ATOLL", "PANAMA", "PAPUA NEW GUINEA", "PARACEL ISLANDS",
                        "PARAGUAY", "PERU", "PHILIPPINES", "PITCAIRN ISLANDS", "POLAND",
                        "PORTUGAL", "PUERTO RICO", "QATAR", "REUNION", "ROMANIA", "RUSSIA",
                        "RWANDA", "ST. HELENA", "ST. KITTS AND NEVIS", "ST LUCIA",
                        "ST. PIERRE AND MIQUELON", "ST. VINCENT/GRENADINES", "WESTERN SAMOA",
                        "SAN MARINO", "SAO TOME AND PRINCIPE", "SAUDI ARABIA", "SENEGAL", "SERBIA",
                        "SEYCHELLES", "SIERRA LEONE", "SINGAPORE", "SLOVAK REPUBLIC", "SLOVENIA",
                        "SOLOMON ISLANDS", "SOMALIA", "SOUTH AFRICA", "S.GEORGIA/S.SANDWIC IS",
                        "SPAIN", "SPRATLY ISLANDS", "SRI LANKA", "SUDAN", "SURINAME", "SVALBARD",
                        "SWAZILAND", "SWEDEN", "SWITZERLAND", "SYRIA", "TAIWAN", "TAJIKISTAN",
                        "TANZANIA", " UNITED REP OF", "THAILAND", "TIMOR-LESTE", "TOGO", "TOKELAU",
                        "TONGA", "TRINIDAD AND TOBAGO", "TROMELIN ISLAND", "TUNISIA", "TURKEY",
                        "TURKMENISTAN", "TURKS AND CAICOS ISL", "TUVALU",
                        "US MISC PACIFIC ISLANDS", "SOVIET UNION", "UGANDA", "UKRAINE",
                        "UNITED ARAB EMIRATES", "UNITED KINGDOM", "U.S. MINOR OUTLYING ISL",
                        "URUGUAY", "UZBEKISTAN", "VANUATU (NEW HEBRIDES)", "VENEZUELA", "VIETNAM",
                        "BRITISH VIRGIN IS.", "VIRGIN ISLANDS", "WAKE ISLAND", "WALLIS AND FUTUNA",
                        "WEST BANK", "WESTERN SAHARA", "YEMEN", "YUGOSLAVIA", "ZAIRE", "ZAMBIA",
                        "ZIMBABWE");
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
        i_state.setValueMap("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
                "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois",
                "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
                "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
                "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
                "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
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
