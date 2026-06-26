package utils;

public class Constants {

    // ==========================================
    //          APPLICATION SETTINGS
    // ==========================================
    public static final String URL = "https://demowebshop.tricentis.com/";
    public static final String EMAIL = "mohamedsameh20062@gmail.com";
    public static final String PASSWORD = "789123";

    // Navigation Paths
    public static final String LOGIN_PATH = "login";
    public static final String CART_PATH = "cart";
    public static final String NOTEBOOKS_PATH = "notebooks";

    // ==========================================
    //        SHOPPING CART TEST DATA
    // ==========================================
    public static final String TARGET_PRODUCT = "14.1-inch Laptop";
    public static final String SECONDARY_PRODUCT = "Fiction";

    public static final String PRODUCT_PATH_LAPTOP = "141-inch-laptop";
    public static final String PRODUCT_PATH_SECONDARY = "fiction";
    public static final String PRODUCT_PATH_OUT_OF_STOCK = "custom-t-shirt";

    // ==========================================
    //        QUANTITY TEST DATA
    // ==========================================
    public static final String QTY_ZERO = "0";
    public static final String QTY_TWO = "2";
    public static final String QTY_THREE = "3";
    public static final String QTY_NEGATIVE = "-5";
    public static final String QTY_MAX_LIMIT = "99999";

    // ==========================================
    //         WISHLIST TEST DATA
    // ==========================================
    public static final String WISHLIST_PRODUCT_PRIMARY = "50's Rockabilly Polka Dot Top JR Plus Size";
    public static final String WISHLIST_PRODUCT_SECONDARY = "Blue and green Sneaker";

    public static final String WISHLIST_PRODUCT_URL_PRIMARY = "50s-rockabilly-polka-dot-top-jr-plus-size";
    public static final String WISHLIST_PRODUCT_URL_SECONDARY = "blue-and-green-sneaker";

    // ==========================================
    //         REGISTRATION TEST DATA
    // ==========================================
    public static final String FIRST_NAME = "Test";
    public static final String LAST_NAME = "User";

    // ==========================================
    //      INVALID LOGIN TEST DATA & ERRORS
    // ==========================================
    // Inputs
    public static final String INVALID_EMAIL_FORMAT = "invalid_email_format";
    public static final String UNREGISTERED_EMAIL = "unregistered@mail.com";
    public static final String EMPTY_EMAIL = "";
    public static final String WRONG_PASSWORD = "WrongPassword!";

    // Expected Error Messages
    public static final String ERR_INVALID_EMAIL = "Please enter a valid email address.";
    public static final String ERR_UNSUCCESSFUL_LOGIN = "Login was unsuccessful. Please correct the errors and try again.";
    public static final String ERR_NO_ACCOUNT = "No customer account found";


    // ==========================================
    //      REGISTRATION ERROR MESSAGES
    // ==========================================
    public static final String ERR_REQ_FIRSTNAME = "First name is required.";
    public static final String ERR_REQ_LASTNAME = "Last name is required.";
    public static final String ERR_REQ_EMAIL = "Email is required.";
    public static final String ERR_REQ_PASSWORD = "Password is required.";
    public static final String ERR_REQ_CONFIRMPASSWORD = "Password is required."; // Note: Adjust this if DemoWebShop expects "The password and confirmation password do not match."


}