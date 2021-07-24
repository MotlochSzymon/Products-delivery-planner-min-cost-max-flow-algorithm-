package inputElements;

public class ProducerPharmacyConnection {

    private int producentId;
    private int pharmacyId;
    private int maxDailyDeliveredVaccines;
    private double vaccineCost;

    public ProducerPharmacyConnection(int producentId, int pharmacyId, int maxDailyDeliveredVaccines, double vaccineCost) {
        this.producentId = producentId;
        this.pharmacyId = pharmacyId;
        this.maxDailyDeliveredVaccines = maxDailyDeliveredVaccines;
        this.vaccineCost = vaccineCost;
    }

    public int getProducerId() {
        return this.producentId;
    }

    public int getPharmacyId() {
        return this.pharmacyId;
    }

    public int getMaxDailyDeliveredVaccines() {
        return this.maxDailyDeliveredVaccines;
    }

    public double getVaccineCost() {
        return this.vaccineCost;
    }

    public void setVaccineCost(double cost) {
        this.vaccineCost = cost;
    }

}
