package nagma_3.com.example.tutorial2.model;

public class UserDetailsEntity {
//    private String userId;
//    private String userName;
//    private String phone;
      private String a_id;
      private String accValuex;
      private String accValuey;
      private String accValuez;
      private String gyroValuex;
      private String gyroValuey;
      private String gyroValuez;
      private String latitude;
      private String longitude;
      private String speedfinal;
      private String annotate;

    public UserDetailsEntity(String accValuex, String accValuey, String accValuez, String gyroValuex, String gyroValuey, String gyroValuez, String latitude, String longitude, String speedfinal, String annotate) {
        this.accValuex = accValuex;
        this.accValuey = accValuey;
        this.accValuez = accValuez;
        this.gyroValuex = gyroValuex;
        this.gyroValuey = gyroValuey;
        this.gyroValuez = gyroValuez;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedfinal = speedfinal;
        this.annotate = annotate;

    }




    //    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }



    public String geta_id() {
        return a_id;
    }

    public void seta_id(String a_id) {
        this.a_id = a_id;
    }

    public String getaccValuex() {
        return accValuex;
    }

    public void setaccValuex(String accValuex) {
        this.accValuex = accValuex;
    }

    public String getaccValuey() {
        return accValuey;
    }

    public void setaccValuey(String accValuey) {
        this.accValuey = accValuey;
    }

    public String getaccValuez() {
        return accValuez;
    }

    public void setaccValuez(String accValuez) {
        this.accValuez = accValuez;
    }

    public String getgyroValuex() {
        return gyroValuex;
    }

    public void setgyroValuex(String gyroValuex) {
        this.gyroValuex = gyroValuex;
    }

    public String getgyroValuey() {
        return gyroValuey;
    }

    public void setgyroValuey(String gyroValuey) {
        this.gyroValuey = gyroValuey;
    }

    public String getgyroValuez() {
        return gyroValuez;
    }

    public void setgyroValuez(String gyroValuez) {
        this.gyroValuez = gyroValuez;
    }

    public String getlatitude() {
        return latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpeedfinal() {
        return speedfinal;
    }

    public void setSpeedfinal(String speedo) {
        this.speedfinal = speedfinal;
    }

    public String getannotate() {
        return annotate;
    }

    public void setannotate(String annotate) {
        this.annotate = annotate;
    }



}
