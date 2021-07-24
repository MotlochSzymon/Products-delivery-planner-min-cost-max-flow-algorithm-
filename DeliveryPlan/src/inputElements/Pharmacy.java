package inputElements;

public class Pharmacy {

    private int pharmacyId;
    private String pharmacyName;
    private int dayilyNeeds;

    public Pharmacy(int pharmacyId, String pharmacyName, int dayilyNeeds) {
        this.pharmacyId = pharmacyId;
        this.pharmacyName = pharmacyName;
        this.dayilyNeeds = dayilyNeeds;
    }

    public int getPharmacyId() {
        return this.pharmacyId;
    }

    public int getDayilyNeeds() {
        return this.dayilyNeeds;
    }

    public String getPharmacyName() {
        return this.pharmacyName;
    }
}
