package inputAndOutputHandling;

import inputElements.Pharmacy;
import inputElements.ProducerPharmacyConnection;
import inputElements.VaccineProducer;
import java.util.List;

public interface PharmacyProducerInputDataInterface {

    public void loadAllFileRows(String filePath) throws Exception;

    public List<VaccineProducer> getVaccineProducerList();

    public List<Pharmacy> getPharmacyList();

    public List<ProducerPharmacyConnection> getProducerPharmacyList();
}
