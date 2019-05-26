export class CONSTANTS {
    
    public static MAX_FILE_UPLOAD_SIZE_LIMIT = 4000000;
    public static COOKIE_ACCESSTOKEN = "accessToken";
    public static COOKIE_ENCRYPTED_ID = "encryptedId";
    public static COOKIE_USER_ROLE = "userRole";
    public static USER = "user";
    public static LOGIN_COOKIE_EXPIRATION_DAYS : number = 7;
    public static ENCRYPT_DECRYPT_SECRET_KEY : string = "1234567890";
    public static PATH_OF_COOKIE : string = "pathOfCookie";
    public static CRYPTO_SECRET_KEY = 'cryptoSecretKey';
    public static PERMISSIONS = 'permissions';
    public static USER_ROLE = 'role';
    public static PAGE_NUMBER_LIMIT_OPTIONS = [10, 15, 20];
    public static BRAND : string = 'brand';
    public static BRAND_ID : string = 'brandId';
    public static SUBMIT : string = 'Submit';
    public static SUBMITTING : string = 'Submitting...';
    
    //Uploading constants
    public static blockUIMessage : string = 'Please wait while uploading data, it may take several minutes.'
    public static invalidCsvMessage : string = 'Invalid csv file'
}